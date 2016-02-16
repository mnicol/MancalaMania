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

public class studPlayer extends Player {

	/*
	 * Use IDS search to find the best move. The step starts from 1 and
	 * increments by step 1.Note that the search can be interrupted by time
	 * limit.
	 */
	// Alpha-Beta Search
	public void move(GameState state) {

	}

	// Return best move for max player. Note that this is a wrapper function
	// created for ease to use.

	public int maxAction(GameState state, int maxDepth) {
		return maxAction(state, 0, maxDepth, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}

	// return best move for min player. Note that this is a wrapper function
	// created for ease to use.
	public int minAction(GameState state, int maxDepth) {
		return minAction(state, 0, maxDepth, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
	}

	// return best move for max player
	public int maxAction(GameState state, int currentDepth, int maxDepth,
			int alpha, int beta) {
		if (currentDepth == maxDepth || state.status() != Integer.MIN_VALUE) {
			return sbe(state);
		}

		int bestVal = Integer.MIN_VALUE;

		for (int i = 0; i < 6; i++) {
			if (!state.illegalMove(i)) {
				GameState temp = new GameState(state);

				if (temp.applyMove(i)) {
					bestVal = Math.max(
							bestVal,
							maxAction(temp, currentDepth + 1, maxDepth, alpha,
									beta));
				} else {
					bestVal = Math.max(
							bestVal,
							minAction(temp, currentDepth + 1, maxDepth, alpha,
									beta));
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
	public int minAction(GameState state, int currentDepth, int maxDepth,
			int alpha, int beta) {
		if (currentDepth == maxDepth || state.status() != Integer.MIN_VALUE) {
			return sbe(state);
		}

		int bestVal = Integer.MAX_VALUE;

		for (int i = 7; i < 13; i++) {
			if (!state.illegalMove(i)) {
				GameState temp = new GameState(state);

				if (temp.applyMove(i)) {
					bestVal = Math.min(
							bestVal,
							minAction(temp, currentDepth + 1, maxDepth, alpha,
									beta));
				} else {
					bestVal = Math.min(
							bestVal,
							maxAction(temp, currentDepth + 1, maxDepth, alpha,
									beta));
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
	private static int[] weights = { 6, 5, 4, 3, 2, 1, 20 };
	private int sbe(GameState state) {
		int toReturn = getWeightedTotal(state, weights);
		toReturn -= getWeightedTotal(state, weights);

		return toReturn;
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

}
