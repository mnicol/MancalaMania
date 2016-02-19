import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;


public class smarterTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StandardFighters optFigher = new StandardFighters();
		
		
		long startTime = System.currentTimeMillis();
		PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream("smarter"+startTime + ".txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int numRandos = 100;
		int numIters = 500;
		Colosseum colosseum = new Colosseum(7, numRandos, 0, null);
		
		optFigher.CreateRandomWeights();
		
		optFigher = colosseum.KeepOnSpawningAndFighting(optFigher, numIters);
//		ArrayList<StandardFighters> bestFighters = colosseum.BrawlAndGetMostBadAssMother();
//		for (int i = 0; i < bestFighters.size(); i ++){
//			System.out.print("Wins: " + bestFighters.get(i).GamesWon + " Losses:" + bestFighters.get(i).GamesLost + " ");
//			for (int j =0; j < TestSBE.NumWeights; j ++){
//				System.out.print(bestFighters.get(i).Weights[j] );
//				if (j < TestSBE.NumWeights -1){
//					System.out.print(", ");
//				}
//			}
//			System.out.println("\n");
//		}
		System.out.print("Wins: " + optFigher.GamesWon + " Losses:" + optFigher.GamesLost + " ");
		for (int j =0; j < TestSBE.NumWeights; j ++){
			System.out.print(optFigher.Weights[j] );
			if (j < TestSBE.NumWeights -1){
				System.out.print(", ");
			}
		}
		System.out.println("\n");
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

}
