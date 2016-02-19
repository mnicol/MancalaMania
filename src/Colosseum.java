import java.util.ArrayList;
import java.util.Random;

public class Colosseum 
{

	private int searchDepth;
	private int numRandos;
	ArrayList<StandardFighters> UserDefinDefinedPlayers;
	ArrayList<StandardFighters> Randos;
	public Colosseum(int searchDepthP, int numRands, int numUserDef, ArrayList<StandardFighters> definedPlayers){
		searchDepth = searchDepthP;
		numRandos = numRands;
		UserDefinDefinedPlayers = new ArrayList<>();
		Randos = new ArrayList<>();
		for(int i = 0; i < numRandos; i ++){
			
			StandardFighters temp = new StandardFighters();
			temp.CreateRandomWeights();
			Randos.add(temp);
		}
		
	}
	
	//TODO add the user defined fighter logic
	public ArrayList<StandardFighters> BrawlAndGetMostBadAssMother(){
		ArrayList<StandardFighters> toReturn = new ArrayList<StandardFighters>();
		for (int i = 0; i < numRandos; i ++ ){
			for (int j = i; j < numRandos; j ++){
				StandardFighters p1 = Randos.get(i);
				StandardFighters p2 = Randos.get(j);
				GameStats temp = OneOnOneBrawl(p1, p2, searchDepth);
				temp.rivalIndex = j;
				if (i ==j ){
					temp.won = 0;
				}
				if (temp.won == 1){
					p1.GamesWon ++;
					p2.GamesLost ++;
				}
				else if(temp.won == -1){
					p1.GamesLost++;
					p2.GamesWon++;
				}
				else{
					p1.GamesTied ++;
					p2.GamesTied ++;
				}
				p1.gameStats.add(temp);
				temp = temp.InverseValues();
				temp.rivalIndex = i;
				
			}
		}
		int maxWins = 0;
		for (int i = 0; i < Randos.size(); i ++){
			maxWins = Math.max(maxWins, Randos.get(i).GamesWon);
		}
		for (int i = 0; i < Randos.size(); i ++){
			if(Randos.get(i).GamesWon  == maxWins){
				toReturn.add(Randos.get(i));
			}
		}
		
		return toReturn;
	}
	
	public GameStats OneOnOneBrawl(StandardFighters p1P, StandardFighters p2P, int depth){
		GameState game = new GameState();
		while(game.status() == Integer.MIN_VALUE){
			int moveT = p1P.maxAction(game, depth);
			//keep moving player 1.
			while(game.status() == Integer.MIN_VALUE && game.applyMove(moveT)){
				moveT = p1P.maxAction(game, depth);
			}
			moveT = p2P.minAction(game, depth);
			while(game.status() == Integer.MIN_VALUE && game.applyMove(moveT)){
				moveT = p2P.minAction(game, depth);
			}
		}
		//System.out.println("Print one game finished.");
		return new GameStats(game.status(), game.mancalaOf(6), game.mancalaOf(13), -1);
	}
	
}
