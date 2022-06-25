package aplicacao;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;
import xadrez.XadrezException;

public class Program {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadrez px = new PartidaXadrez();
		
		while(true) {
			try {
			Tela.limpaTela();
			Tela.imprimeTabuleiro(px.getPecas());
			
			System.out.println();
			System.out.print("Origem: ");
			PosicaoXadrez origem = Tela.lerPosicaoXadrez(sc);
			
			boolean[][] possiveisMovimentos = px.movimentosPossiveis(origem);
			Tela.limpaTela();
			Tela.imprimeTabuleiro(px.getPecas(), possiveisMovimentos);
			
			System.out.println();
			System.out.print("Destino: ");
			PosicaoXadrez destino = Tela.lerPosicaoXadrez(sc);
			
			PecaXadrez capturaPeca = px.executaMovimento(origem, destino);
			
			}catch(XadrezException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		
	}
}
