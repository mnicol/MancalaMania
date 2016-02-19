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

public class BasePlayer extends Player  implements WeightableDouble {
public double[] Weights;
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
	public BasePlayer() {
		Weights = new double[TestSBE.NumWeights];
	}
	public BasePlayer(double[] weightsP){
		Weights = weightsP;
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
				if (bestVal > beta) {
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
				if (bestVal > beta) {
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
	private int[] weights = { 6, 5, 4, 3, 2, 1, 20 };
	private int sbe(GameState state) {
						
		return TestSBE.sbe(state, this.Weights);
		
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
		weights = weightsT;
	}
	
	public void SetWeights(double[] weightsT){
		Weights = weightsT;
	}
	public double[] GetWeights(){
		return Weights;
	}
	
}
