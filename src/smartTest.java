import java.util.ArrayList;


public class smartTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		
		int numRandos = 150;
		Colosseum colosseum = new Colosseum(7, numRandos, 0, null);
		ArrayList<StandardFighters> bestFighters = colosseum.BrawlAndGetMostBadAssMother();
		for (int i = 0; i < bestFighters.size(); i ++){
			System.out.print("Wins: " + bestFighters.get(i).GamesWon + " Losses:" + bestFighters.get(i).GamesLost + " ");
			for (int j =0; j < TestSBE.NumWeights; j ++){
				System.out.print(bestFighters.get(i).Weights[j] );
				if (j < TestSBE.NumWeights -1){
					System.out.print(", ");
				}
			}
			System.out.println("\n");
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

}
