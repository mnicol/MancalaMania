import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;


public class smartTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		/*PrintStream out;
		try {
			out = new PrintStream(new FileOutputStream(startTime + ".txt"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}*/

		int numRandos = 0;
		if(args.length == 1)
		    numRandos = Integer.parseInt(args[0]);
		else
		    numRandos = 1000;

		Colosseum colosseum = new Colosseum(7, numRandos, 0, null);
		ArrayList<StandardFighters> bestFighters = colosseum.BrawlAndGetMostBadAssMother();
		for (int i = 0; i < bestFighters.size(); i ++){
		    System.out.println("Number of Players: " + numRandos);
			System.out.print("Wins: " + bestFighters.get(i).GamesWon + " Losses:" + bestFighters.get(i).GamesLost + " ");
			for (int j =0; j < TestSBE.NumWeights; j ++){
				System.out.print(bestFighters.get(i).Weights[j] );
				if (j < TestSBE.NumWeights -1){
					System.out.print(", ");
				}
			}
			System.out.println();
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total Time(ms): " + totalTime);
		System.out.println();
	}

}
