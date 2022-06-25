package aplicacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.Cor;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Tela {
	
	/*
	 * public static final String ANSI_PRETO = "\u001B[30m";
	 * public static final String ANSI_VERMELHO = "\u001B[31m";
	 * public static final String ANSI_VERDE = "\u001B[32m";
	 * public static final String ANSI_AZUL = "\u001B[34m";
	 * public static final String ANSI_ROXO = "\u001B[35m";
	 * public static final String ANSI_CIANO = "\u001B[36m";
	 * 
	 * public static final String ANSI_VERMELHO_BACKGROUND = "\u001B[41m";
	 * public static final String ANSI_VERDE_BACKGROUND = "\u001B[42m";
	 * public static final String ANSI_AZUL_BACKGROUND = "\u001B[44m";
	 * public static final String ANSI_ROXO_BACKGROUND = "\u001B[45m";
	 * public static final String ANSI_CIANO_BACKGROUND = "\u001B[46m";
	 * 
	 */
	
	public static final String ANSI_RESET = "\u001B[0m";	
	public static final String ANSI_AMARELO = "\u001B[33m";	
	public static final String ANSI_BRANCO = "\u001B[37m";

	public static final String ANSI_PRETO_BACKGROUND = "\u001B[40m";	
	public static final String ANSI_AMARELO_BACKGROUND = "\u001B[43m";	
	public static final String ANSI_BRANCO_BACKGROUND = "\u001B[47m";
	
	// Limpar o console
	public static void limpaTela() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	public static PosicaoXadrez lerPosicaoXadrez(Scanner sc) {
		try {
			String s = sc.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1));
			return new PosicaoXadrez(coluna, linha);
		}catch(RuntimeException e) {
			throw new InputMismatchException("Erro ao ler PosicaoXadrez. Valores validos sao de a1 ate h8.");
		}
	}
	
	public static void imprimeTabuleiro(PecaXadrez[][] pecas) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pecas.length; j++) {
				imprimePeca(pecas[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}

	private static void imprimePeca(PecaXadrez peca) {
		if (peca == null) {
			System.out.print("-");
		} else {
			if(peca.getCor() == Cor.BRANCO) {
				System.out.print(ANSI_BRANCO + peca + ANSI_RESET);
			}else {
				System.out.print(ANSI_AMARELO + peca + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}
}
