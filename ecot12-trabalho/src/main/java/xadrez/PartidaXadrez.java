package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Rainha;
import xadrez.pecas.Peao;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tab;
	private boolean xeque;
	private boolean xequeMate;

	private List<Peca> pecasNoTabuleiro = new ArrayList<>();
	private List<Peca> pecasCapturadas = new ArrayList<>();

	public PartidaXadrez() {
		tab = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.BRANCO;
		PosicaoInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getXeque() {
		return xeque;
	}

	public boolean getXequeMate() {
		return xequeMate;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tab.getLinhas()][tab.getColunas()];
		for (int i = 0; i < tab.getLinhas(); i++) {
			for (int j = 0; j < tab.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tab.peca(i, j);
			}
		}
		return mat;
	}

	public boolean[][] movimentosPossiveis(PosicaoXadrez posicaoOrigem) {
		Posicao pos = posicaoOrigem.toPosicao();
		validaPosicaoOrigem(pos);
		return tab.peca(pos).possiveisMovimentos();
	}

	public PecaXadrez executaMovimento(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoDestino) {
		Posicao origem = posicaoOrigem.toPosicao();
		Posicao destino = posicaoDestino.toPosicao();
		validaPosicaoOrigem(origem);
		validaPosicaoDestivo(origem, destino);
		Peca pecaCapturada = fazMovimento(origem, destino);

		if (testeXeque(jogadorAtual)) {
			desfazMovimento(origem, destino, pecaCapturada);
			throw new XadrezException("voce nao se colocar em xeque");
		}

		xeque = (testeXeque(oponente(jogadorAtual))) ? true : false;

		if (testeXequeMate(oponente(jogadorAtual))) {
			xequeMate = true;
		} else {
			proximoTurno();
		}
		return (PecaXadrez) pecaCapturada;
	}

	private void validaPosicaoDestivo(Posicao origem, Posicao destino) {
		if (!tab.peca(origem).possivelMovimento(destino)) {
			throw new XadrezException("A peca escolhida nao pode mover para a posicao destino");
		}
	}

	private Peca fazMovimento(Posicao origem, Posicao destino) {
		PecaXadrez p = (PecaXadrez) tab.removePeca(origem);
		p.aumentaContadorMovimento();
		Peca pecacapturada = tab.removePeca(destino);
		tab.posicaoPeca(p, destino);

		if (pecasCapturadas != null) {
			pecasNoTabuleiro.remove(pecacapturada);
			pecasCapturadas.add(pecacapturada);

		}

		// #Movimento especial roque pequeno
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tab.removePeca(origemT);
			tab.posicaoPeca(torre, destinoT);
			torre.aumentaContadorMovimento();
		}

		// #Movimento especial roque grande
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tab.removePeca(origemT);
			tab.posicaoPeca(torre, destinoT);
			torre.aumentaContadorMovimento();
		}

		return pecacapturada;
	}

	private void desfazMovimento(Posicao origem, Posicao destino, Peca pecaCapturada) {
		PecaXadrez p = (PecaXadrez) tab.removePeca(destino);
		p.diminuiContadorMovimento();
		tab.posicaoPeca(p, origem);

		if (pecaCapturada != null) {
			tab.posicaoPeca(pecaCapturada, destino);
			pecasCapturadas.remove(pecaCapturada);
			pecasNoTabuleiro.add(pecaCapturada);
		}

		// #Movimento especial roque pequeno
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() + 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tab.removePeca(origemT);
			tab.posicaoPeca(torre, destinoT);
			torre.aumentaContadorMovimento();
		}

		// #Movimento especial roque grande
		if (p instanceof Rei && destino.getColuna() == origem.getColuna() - 2) {
			Posicao origemT = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao destinoT = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tab.removePeca(origemT);
			tab.posicaoPeca(torre, destinoT);
			torre.aumentaContadorMovimento();
		}
	}

	private void validaPosicaoOrigem(Posicao pos) {
		if (!tab.existePeca(pos)) {
			throw new XadrezException("Nao existe peca na posicao de origem");
		}
		if (jogadorAtual != ((PecaXadrez) tab.peca(pos)).getCor()) {
			throw new XadrezException("A peca escolhida nao e sua");

		}
		if (!tab.peca(pos).existeMovimentoPossivel()) {
			throw new XadrezException("Nao existe movimentos possiveis para a peca escolhida");
		}

	}

	private Cor oponente(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.PRETO;
	}

	private PecaXadrez rei(Cor cor) {
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Nao existe rei " + cor + " no tabuleiro.");
	}

	private boolean testeXeque(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
		List<Peca> pecaOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());
		for (Peca p : pecaOponente) {
			boolean[][] mat = p.possiveisMovimentos();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testeXequeMate(Cor cor) {
		if (!testeXeque(cor)) {
			return false;
		}
		List<Peca> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.possiveisMovimentos();
			for (int i = 0; i < tab.getLinhas(); i++) {
				for (int j = 0; j < tab.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez) p).getPosicaoXadrez().toPosicao();
						Posicao destino = new Posicao(i, j);
						Peca pecaCapturada = fazMovimento(origem, destino);
						boolean testeXeque = testeXeque(cor);
						desfazMovimento(origem, destino, pecaCapturada);
						if (!testeXeque) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	private void proximoTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;

	}

	private void colocaNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tab.posicaoPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);

	}

	private void PosicaoInicial() {

		colocaNovaPeca('a', 1, new Torre(tab, Cor.BRANCO));
		colocaNovaPeca('b', 1, new Cavalo(tab, Cor.BRANCO));
		colocaNovaPeca('c', 1, new Bispo(tab, Cor.BRANCO));
		colocaNovaPeca('d', 1, new Rainha(tab, Cor.BRANCO));
		colocaNovaPeca('e', 1, new Rei(tab, Cor.BRANCO, this));
		colocaNovaPeca('f', 1, new Bispo(tab, Cor.BRANCO));
		colocaNovaPeca('g', 1, new Cavalo(tab, Cor.BRANCO));
		colocaNovaPeca('h', 1, new Torre(tab, Cor.BRANCO));
		colocaNovaPeca('a', 2, new Peao(tab, Cor.BRANCO));
		colocaNovaPeca('b', 2, new Peao(tab, Cor.BRANCO));
		colocaNovaPeca('c', 2, new Peao(tab, Cor.BRANCO));
		colocaNovaPeca('d', 2, new Peao(tab, Cor.BRANCO));
		colocaNovaPeca('e', 2, new Peao(tab, Cor.BRANCO));
		colocaNovaPeca('f', 2, new Peao(tab, Cor.BRANCO));
		colocaNovaPeca('g', 2, new Peao(tab, Cor.BRANCO));
		colocaNovaPeca('h', 2, new Peao(tab, Cor.BRANCO));

		colocaNovaPeca('a', 8, new Torre(tab, Cor.PRETO));
		colocaNovaPeca('b', 8, new Cavalo(tab, Cor.PRETO));
		colocaNovaPeca('c', 8, new Bispo(tab, Cor.PRETO));
		colocaNovaPeca('d', 8, new Rainha(tab, Cor.PRETO));
		colocaNovaPeca('e', 8, new Rei(tab, Cor.PRETO, this));
		colocaNovaPeca('f', 8, new Bispo(tab, Cor.PRETO));
		colocaNovaPeca('g', 8, new Cavalo(tab, Cor.PRETO));
		colocaNovaPeca('h', 8, new Torre(tab, Cor.PRETO));
		colocaNovaPeca('a', 7, new Peao(tab, Cor.PRETO));
		colocaNovaPeca('b', 7, new Peao(tab, Cor.PRETO));
		colocaNovaPeca('c', 7, new Peao(tab, Cor.PRETO));
		colocaNovaPeca('d', 7, new Peao(tab, Cor.PRETO));
		colocaNovaPeca('e', 7, new Peao(tab, Cor.PRETO));
		colocaNovaPeca('f', 7, new Peao(tab, Cor.PRETO));
		colocaNovaPeca('g', 7, new Peao(tab, Cor.PRETO));
		colocaNovaPeca('h', 7, new Peao(tab, Cor.PRETO));

	}
}
