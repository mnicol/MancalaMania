/****************************************************************
 * studPlayer.java Implements MiniMax search with A-B pruning and iterative
 * deepening search (IDS). The static board evaluator (SBE) function is simple:
 * the # of stones in studPlayer's mancala minue the # in opponent's mancala.
 * ----
 * --------------------------------------------------------------------------
 * ----------------------------------- Licensing Information: You are free to
 * use or extend these projects for educational purposes provided that (1) you
 * do not distribute or publish solutions, (2) you retain the notice, and (3)
 * you provide clear attribution to UW-Madison
 * 
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 * 
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his
 * TAs.
 * 
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu). Some
 * GUI componets are from Mancala Project in Google code.
 */

// ################################################################
// studPlayer class
// ################################################################

public class aklapperPlayer extends Player {

	/*
	 * Use IDS search to find the best move. The step starts from 1 and
	 * increments by step 1.Note that the search can be interrupted by time
	 * limit.
	 */
	// Alpha-Beta Search
	public void move(GameState state) {
		//I don't know whether or not to call on max action or min action here.
		int currentDepth = 1;
		while (true){
			move = maxAction(state, currentDepth);
			currentDepth ++;
		}
		
	}
   
	// Return best move for max player. Note that this is a wrapper function
	// created for ease to use.

	public int maxAction(GameState state, int maxDepth) {
		//TODO should an exception be thrown here if maxDepth is not >= 0, or the game is finished?
		
		int playerMove = -1;
		int bestVal = Integer.MIN_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		for (int i = 0; i < 6; i++) {
			if (!state.illegalMove(i)) {
				GameState temp = new GameState(state);

				if (temp.applyMove(i)) {
					bestVal = Math.max(bestVal,maxAction(temp, 1, maxDepth, alpha, beta));
				} else {
					bestVal = Math.max(bestVal,minAction(temp, 1, maxDepth, alpha, beta));
				}
				//I believe it is impossible to enter this if  statement.
				if (bestVal >= beta) {
					 return i;
				}
				
				//update  the player move if a new best move is found.
				if (alpha < bestVal){
					playerMove = i;
					alpha = bestVal;
				}
			}
		}
		
		
		return playerMove;
	}

	// return best move for min player. Note that this is a wrapper function
	// created for ease to use.
	public int minAction(GameState state, int maxDepth) {
		int playerMove = -1;
		
		int bestVal = Integer.MAX_VALUE;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		for (int i = 7; i < 13; i++) {
			if (!state.illegalMove(i)) {
				GameState temp = new GameState(state);
					
				if (temp.applyMove(i)) {
					bestVal = Math.min(bestVal,minAction(temp, 1, maxDepth, alpha, beta));
				} else {
					bestVal = Math.min(bestVal,maxAction(temp, 1, maxDepth, alpha, beta));
				}
				if (bestVal <= alpha) {
					return i;
				}
				if(bestVal < beta){
					playerMove = i;
					beta = bestVal;
				}
								
			}
		}
		return playerMove;
	}

	// return best move for max player
	public int maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta) {
		if (currentDepth == maxDepth || state.status() != Integer.MIN_VALUE) {
			return sbe(state);
		}

		int bestVal = Integer.MIN_VALUE;

		for (int i = 0; i < 6; i++) {
			if (!state.illegalMove(i)) {
				GameState temp = new GameState(state);

				if (temp.applyMove(i)) {
					bestVal = Math.max(bestVal,	maxAction(temp, currentDepth + 1, maxDepth, alpha, beta));
				} 
				else {
					bestVal = Math.max(bestVal,minAction(temp, currentDepth + 1, maxDepth, alpha, beta));
				}
				if (bestVal >= beta) {
					return bestVal;
				}
				alpha = Math.max(alpha, bestVal);
			}
		}
		return bestVal;

	}

	// return best move for min player
	public int minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta) {
		if (currentDepth == maxDepth || state.status() != Integer.MIN_VALUE) {
			return sbe(state);
		}

		int bestVal = Integer.MAX_VALUE;

		for (int i = 7; i < 13; i++) {
			if (!state.illegalMove(i)) {
				GameState temp = new GameState(state);

				if (temp.applyMove(i)) {
					bestVal = Math.min(bestVal,minAction(temp, currentDepth + 1, maxDepth, alpha, beta));
				} else {
					bestVal = Math.min(bestVal,maxAction(temp, currentDepth + 1, maxDepth, alpha, beta));
				}
				if (bestVal <= alpha) {
					return bestVal;
				}
				beta = Math.min(beta, bestVal);
			}
		}
		return bestVal;
	}

	// the sbe function for game state. Note that in the game state, the bins
	// for current player are always in the bottom row.
	private double[] weights = { 4.101819938063927, 2.1540418682151703, 4.996014057746573, 1.2351047244037001, 2.0282690010837894, 3.7267145278542966, 2.1410130043309583, 2.055379678629751, 0.8650190659315681, 1.815060107403754, 0.10726620969909961, 2.170883121304058 };
	private int sbe(GameState state) {
		//int toReturn = getWeightedTotal(state, weights);
		
		return sbe(state, weights);
	}

	/**
	 * Calculates a weighted sum of the mancalas on your side of the board.
	 * 
	 * @param startBin asdfadf
	 * @return weighted sum of 
	 */
	private int getWeightedTotal(GameState state, int[] weights) {
		//int[] weights = { 6, 5, 4, 3, 2, 1, 20 };
		int toReturn = 0;
		for (int i = 0; i < weights.length; i++) {
			toReturn += (state.stoneCount(i) * weights[i]) - (state.stoneCount(i + weights.length) * weights[i]);
		}
		return toReturn;

	}
	public void SetWeights(int[] weightsT){
		
	}
	
	 public static int sbe(GameState state, double[] weights)
	    {
	    	int SbeValue = 0;
	    	
	    	// Add advantage from being able to land in your mancala
	    	SbeValue += landInMancala(state, weights[0]);
	    	
	    	//Checks to see if there is one stone two bins away from mancala
	    	SbeValue +=  checkTwoOut(state, weights[1]);
	    	
	    	// Add advantage from not having pieces in your bin before mancala
	    	SbeValue += emptyPreMancala(state, weights[2]);
	    	
	    	// Add advantage of potentially being able to steal beans
	    	SbeValue += stealPotential(state, weights[3]);
	    	
	    	// Add advantage from having more pieces
	    	SbeValue += pieceDistribution(state, weights[4]);
	    	
	    	// Determine what player has placed more stones in their mancala
	    	SbeValue += pointDifferental(state, weights[5]);
	    	
	    	// Determine if move will secure victory
	    	SbeValue += halfInBin(state);
	    	
	    	//Single Bin Functions
	    	
	    	// Calculate value from difference in first bin
	    	SbeValue += firstBin(state, weights[6]);
	    	
	    	// Calculate value from difference in first bin
	    	SbeValue += secondBin(state, weights[7]);
	    	
	    	// Calculate value from difference in first bin
	    	SbeValue += thirdBin(state, weights[8]);
	    	
	    	// Calculate value from difference in first bin
	    	SbeValue += fourthBin(state, weights[9]);
	    	
	    	// Calculate value from difference in first bin
	    	SbeValue += fifthBin(state, weights[10]);
	    	
	    	// Calculate value from difference in first bin
	    	SbeValue += sixthBin(state, weights[11]);
	    	
	    	return SbeValue;
	    }
	    
	    // SBE Helper Funcitons
	    private static int landInMancala(GameState state, double weight)
	    {
	    	int numberThatCanLandInMancala = 0;
	    	
	    	// Player A (+)
	    	for(int i = 0; i <6; i++)
	    	{
	    		if((state.stoneCount(i) - (6 - i)) % 13 == 0)
	    			numberThatCanLandInMancala = numberThatCanLandInMancala +
	    					(((state.stoneCount(i) - (6 - i)) / 13) + 1);
	    	}
	    	
	    	// Player B (-)
	    	for(int i = 7; i <13; i++)
	    	{
	    		if((state.stoneCount(i) - (13 - i)) % 13 == 0)
	    			numberThatCanLandInMancala = numberThatCanLandInMancala - 
	    					(((state.stoneCount(i) - (13 - i)) / 13) + 1);
	    	}
	    	
	    	return (int)(numberThatCanLandInMancala * weight);
	    }
	    
	    /**
	     * Checks to see if there is one stone two bins away from mancala
	     * @param state
	     * @return
	     */
	    private static int checkTwoOut(GameState state, double weight)
	    {
	    	double advantage = 0;
	    	
	    	if(state.stoneCount(4) == 1)
		    	advantage = advantage + (1 * weight);
	    	if(state.stoneCount(11) == 1)
	    		advantage = advantage - (1 * weight);
	    	
	    	return (int)advantage;
	    }
	    
	    /**
	     * Checks to see if the space before the mancala is empty
	     * @param state
	     * @return
	     */
	    private static int emptyPreMancala(GameState state, double weight)
	    {
	    	double advantage = 0;
	    	
	    	if(state.stoneCount(5) == 0)
		    	advantage = advantage + (1 * weight);
	    	if(state.stoneCount(12) == 0)
	    		advantage = advantage - (1 * weight);
	    	
	    	return (int)advantage;
	    }
	    
	    /**
	     * Checks to see if a player has the ability to steal and weights based on stones stolen
	     * @param state
	     * @return
	     */
	    private static int stealPotential(GameState state, double weight)
	    {
	    	int stealPotential = 0;
	    	
	    	boolean[] stolenFrom = {false, false, false, false, false, false, false, false, false, false, false, false, false};
	    	
	    	// Player A (+)
	    	for(int i = 0; i <= 6; i++)
	    	{
	    		int index = 0;
	    		
	    		if(state.stoneCount(i) <= 6 && state.stoneCount(i) > 0)
	    		{
	    			index = i + state.stoneCount(i);
	    		}
	    		else if(state.stoneCount(i) >= 8 && state.stoneCount(i) < 13)
	    		{
	    			index = (state.stoneCount(i) - 13) + i;
	    		}
	    		else
	    		{
	    			index = -1;
	    		}
	    		
	    		if(index >= 0 && index < 6 && state.stoneCount(index) == 0 && stolenFrom[index] == false && (state.stoneCount(state.neighborOf(index)) > 0 || state.stoneCount(i) >=8))
	    		{
	    			stolenFrom[index] = true;
	    			if(state.stoneCount(i) >= 8)
	    				stealPotential = stealPotential + state.stoneCount(state.neighborOf(index)) + 2;
	    			else
	    				stealPotential = stealPotential + state.stoneCount(state.neighborOf(index)) + 1;
	    		}
	    	}
	    	
	    	// Player B (-)
	    	for(int i = 7; i <= 13; i++)
	    	{
	    		int index = 0;
	    		
	    		if(state.stoneCount(i) <= 6 && state.stoneCount(i) > 0)
	    		{
	    			index = i + state.stoneCount(i);
	    		}
	    		else if(state.stoneCount(i) >= 8 && state.stoneCount(i) < 13)
	    		{
	    			index = (state.stoneCount(i) - 13) + i;
	    		}
	    		else
	    		{
	    			index = -1;
	    		}
	    		
	    		if(index < 13 && index > 6 && state.stoneCount(index) == 0 && stolenFrom[index] == false && (state.stoneCount(state.neighborOf(index)) > 0 || state.stoneCount(i) >=8))
	    		{
	    			stolenFrom[index] = true;
	    			if(state.stoneCount(i) >= 8)
	    				stealPotential = stealPotential - state.stoneCount(state.neighborOf(index)) - 2;
	    			else
	    				stealPotential = stealPotential - state.stoneCount(state.neighborOf(index)) - 1;
	    		}
	    	}
	    	
	    	return (int)(stealPotential * weight);
	    }
	    
	    /**
	     * See who has more stones on their side of the board (Includes Mancala)
	     * @param state
	     * @return
	     */
	    private static int pieceDistribution(GameState state, double weight)
	    {
	    	
	    	int playerAStones = 0;
	    	for(int i = 0; i <=6; i++)
	    	{
	    		playerAStones = playerAStones + state.stoneCount(i);
	    	}
	    	
	    	int playerBStones = 0;
	    	for(int i = 7; i <=13; i++)
	    	{
	    		playerBStones = playerBStones + state.stoneCount(i);
	    	}
	    	
	    	return (int)((((double)(playerAStones - playerBStones))/((double)(playerAStones + playerBStones))) * weight * 48);
	    }
	    
	    /**
	     * Determines what player has placed more stones in their mancala
	     * @param state
	     * @return
	     */
	    private static int pointDifferental(GameState state, double weight)
	    {
	    	return (int)((state.stoneCount(6) - state.stoneCount(13)) * weight);
	    }
	    
	    /**
	     * Calculate if move will cause player to win by having more than half the stones
	     * @param state
	     * @return
	     */
	    private static int halfInBin(GameState state)
	    {
	    	int totalStones = 0;
	    	for(int i = 0; i <=13; i++)
	    	{
	    		totalStones += state.stoneCount(i);
	    	}
	    	if(state.stoneCount(6) > (totalStones/2))
	    		return totalStones * 1000;
	    	else if(state.stoneCount(13) > (totalStones/2))
	    		return totalStones * -1000;
	    	else
	    		return 0;
	    	
	    }
	    
	    // Start position functions
	    /**
	     * Determines if the first index gives an advantage
	     * @param state
	     * @return
	     */
	    private static int firstBin(GameState state, double weight)
	    {
	    	return (int)((state.stoneCount(0) - state.stoneCount(7)) * weight);
	    }
	    
	    /**
	     * Determines if the second index gives an advantage
	     * @param state
	     * @return
	     */
	    private static int secondBin(GameState state, double weight)
	    {
	    	return (int)((state.stoneCount(1) - state.stoneCount(8)) * weight);
	    }
	    
	    /**
	     * Determines if the second index gives an advantage
	     * @param state
	     * @return
	     */
	    private static int thirdBin(GameState state, double weight)
	    {
	    	return (int)((state.stoneCount(2) - state.stoneCount(9)) * weight);
	    }
	    
	    /**
	     * Determines if the fourth index gives an advantage
	     * @param state
	     * @return
	     */
	    private static int fourthBin(GameState state, double weight)
	    {
	    	return (int)((state.stoneCount(3) - state.stoneCount(10)) * weight);
	    }
	    
	    /**
	     * Determines if the fifth index gives an advantage
	     * @param state
	     * @return
	     */
	    private static int fifthBin(GameState state, double weight)
	    {
	    	return (int)((state.stoneCount(4) - state.stoneCount(11)) * weight);
	    }
	    
	    /**
	     * Determines if the sixth index gives an advantage
	     * @param state
	     * @return
	     */
	    private static int sixthBin(GameState state, double weight)
	    {
	    	return (int)((state.stoneCount(5) - state.stoneCount(12)) * weight);
	    }
	
}
