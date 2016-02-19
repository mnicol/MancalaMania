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
	private static Random rand = new Random();
	public StandardFighters KeepOnSpawningAndFighting(StandardFighters fighterToOptimize, int numTimesAlterEachVal){
		int chance = (int) Math.round(0.7*numTimesAlterEachVal);
		
		for(int attributeIndex = 0; attributeIndex < TestSBE.NumWeights; attributeIndex ++){
			for (int alteringIndex =0; alteringIndex < numTimesAlterEachVal; alteringIndex ++){
				//make a copy of the current player and then alter one of the values repeatedly then return best.
				StandardFighters temp = new StandardFighters(fighterToOptimize);
				temp.Weights[attributeIndex] += GetRandChange();
		
				//have our optimal fight all, but making first move
				for (int i = 0; i < numRandos; i ++){
					GameStats stat = OneOnOneBrawl(temp, Randos.get(i), searchDepth);
					temp.gameStats.add(stat);
					if(stat.won ==1){
						temp.GamesWon++;
					}
					else if(stat.won ==-1){
						temp.GamesLost++;
					}
					else{
						temp.GamesTied++;
					}
					
				}
				
				//have our optimal fight all, but move second.
				for (int i = 0; i < numRandos; i ++){
					GameStats stat = OneOnOneBrawl(Randos.get(i), temp, searchDepth);
					stat = stat.InverseValues();
					temp.gameStats.add(stat);
					if(stat.won ==-11){
						temp.GamesWon++;
					}
					else if(stat.won ==1){
						temp.GamesLost++;
					}
					else{
						temp.GamesTied++;
					}
					
				}
				//decide whether or not we want to update to the new user
				if(temp.GamesWon > fighterToOptimize.GamesWon){
					fighterToOptimize = temp;
				}
				else if(temp.GamesWon == fighterToOptimize.GamesWon && temp.GamesLost < fighterToOptimize.GamesLost){
					fighterToOptimize = temp;
				}
				else{
					if(UpdateCurrentPlayer(numTimesAlterEachVal, chance, temp.GamesWon, fighterToOptimize.GamesWon)){
						fighterToOptimize = temp;
					}
				}
				
				//update chance
				if (alteringIndex % 2 == 0 || alteringIndex % 3 == 0){
					chance --;
				}
				
			}
		}
		return fighterToOptimize;
	}
	
	private boolean UpdateCurrentPlayer(int numAltersTotal, int curProb, int curWins, int lastWins){
		double percentDrop = ((1.0*lastWins) - curWins)/numRandos;
		if(percentDrop > 0.2){
			return false;
		}
		int randNum = rand.nextInt(numAltersTotal);
		int calcProb = (int) Math.rint(percentDrop* curProb);
		if (randNum < calcProb){
			return true;
		}
		return false;
		
	}
	
	private double GetRandChange(){
		double theRand = rand.nextDouble();
		if(rand.nextBoolean()){
			theRand = theRand * -1;
		}
		return theRand;
	}
	
	//TODO add the user defined fighter logic
	public ArrayList<StandardFighters> BrawlAndGetMostBadAssMother(){
		ArrayList<StandardFighters> toReturn = new ArrayList<StandardFighters>();
		for (int i = 0; i < numRandos; i ++ ){
			for (int j = 0; j < numRandos; j ++){
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
		return new GameStats(game.status(), game.stoneCount(6), game.stoneCount(13), -1);
	}
	
}
