package xadrez;

import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {
	private Tabuleiro tab;
	
	public PartidaXadrez() {
		tab = new Tabuleiro(8, 8);
		PosicaoInicial();
	}
	
	public PecaXadrez[][] getPecas(){
		PecaXadrez[][] mat = new PecaXadrez[tab.getLinhas()][tab.getColunas()];
		for (int i = 0; i < tab.getLinhas(); i++) {
			for (int j = 0; j < tab.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tab.peca(i,j);
			}
		}
		return mat;
	}
	
	private void colocaNovaPeca(char coluna, int linha, PecaXadrez peca) {
		tab.posicaoPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());

	}
	
	private void PosicaoInicial() {
	
		colocaNovaPeca('c', 2, new Torre(tab, Cor.BRANCO));
		colocaNovaPeca('d', 2, new Torre(tab, Cor.BRANCO));
		colocaNovaPeca('e', 2, new Torre(tab, Cor.BRANCO));
		colocaNovaPeca('e', 1, new Torre(tab, Cor.BRANCO));
		colocaNovaPeca('c', 1, new Torre(tab, Cor.BRANCO));
		colocaNovaPeca('d', 1, new Rei(tab, Cor.BRANCO));
		
		colocaNovaPeca('d', 8, new Rei(tab, Cor.PRETO));
		colocaNovaPeca('c', 7, new Torre(tab, Cor.PRETO));
		colocaNovaPeca('c', 8, new Torre(tab, Cor.PRETO));
		colocaNovaPeca('d', 7, new Torre(tab, Cor.PRETO));
		colocaNovaPeca('e', 7, new Torre(tab, Cor.PRETO));
		colocaNovaPeca('e', 8, new Torre(tab, Cor.PRETO));
		
	}
}
