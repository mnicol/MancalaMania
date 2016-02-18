import java.util.ArrayList;

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

public class playerAphaBetaTest extends Player implements WeightAble {

	/*
	 * Use IDS search to find the best move. The step starts from 1 and
	 * increments by step 1.Note that the search can be interrupted by time
	 * limit.
	 */
	// Alpha-Beta Search
	public void move(GameState state) {
		//I don't know whether or not to call on max action or min action here.
		int currentDepth = 13;

			move = maxAction(state, currentDepth);

		
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
		System.out.println("ALPHA: " + alpha);
		
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
			System.out.println("Max:" + i);
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
			System.out.println("Min:" + i);
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
	private int[] weights = { 1, 1, 2, 2, 1, 1, 4 };
	private int[] test = {4,6,6,6,6,  7,9,9,9,9,  1,2,2,2,2,  0,1,1,1,1,   8,1,1,1,1,   9,2,2,2,2};
	private int sbe(GameState state) {
		int toReturn = state.stoneCount(6) - state.stoneCount(13);
		//System.out.println("SBE " + toReturn);
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
	public void SetWeights(int[] weightsT){
		weights = weightsT;
	}
	private class TreeNode{
		int depth;
		ArrayList<TreeNode> children;
		int childNumber;
		int sbeVal;
		boolean maxNode;
		public TreeNode(int depthP, int numP, int sbeValP, boolean maxType) {
			depth = depthP;
			children = new ArrayList<playerAphaBetaTest.TreeNode>();
			childNumber = numP;
			sbeVal = sbeValP;
			maxNode = maxType;
		}
		
		public void AddChild(TreeNode child){
			children.add(child);
		}
		public void PrintMe(){
			String toPrint = "";
			if(maxNode){
				toPrint += "X:";
			}
			else{
				toPrint += "N:";
			}
			toPrint += "D:" + this.depth +"_" +"C:" + this.childNumber + "_" + "S:" + this.sbeVal + "   ";
			System.out.print(toPrint);
		}
	}
	
	public TreeNode GetOutComes(int depth, GameState game){
		return GetOutcomesHelperMax(0, depth, -1, game);
	}
	
	public TreeNode GetOutcomesHelperMax(int curDepth, int maxDepth, int child, GameState game){
		TreeNode toReturn = new TreeNode(curDepth, child, 0, true);
		if (curDepth == maxDepth){
			toReturn.sbeVal = this.sbe(game);
			return toReturn;
		}
		for (int i = 0; i < 6; i ++){
			if (!game.illegalMove(i)){
				GameState temp = new GameState(game);
				//now get the child and add it to this current node.
				
				if(temp.applyMove(i)){
					toReturn.AddChild(GetOutcomesHelperMax(curDepth +1, maxDepth, i, temp));
				}
				// else get node for other player moving.
				else{
					toReturn.AddChild(GetOutcomesHelperMin(curDepth + 1, maxDepth, i, temp));
				}
				

			}
		}
		
		
		return toReturn;
	}
	
	
	public TreeNode GetOutcomesHelperMin(int curDepth, int maxDepth, int child, GameState game){
		TreeNode toReturn = new TreeNode(curDepth, child, 0, false);
		if (curDepth == maxDepth){
			toReturn.sbeVal = this.sbe(game);
			return toReturn;
		}
		for (int i = 7; i < 13; i ++){
			if (!game.illegalMove(i)){
				GameState temp = new GameState(game);
				//now get the child and add it to this current node.
				
				if(temp.applyMove(i)){
					toReturn.AddChild(GetOutcomesHelperMin(curDepth +1, maxDepth, i, temp));
				}
				// else get node for other player moving.
				else{
					toReturn.AddChild(GetOutcomesHelperMax(curDepth +1, maxDepth, i, temp));
				}
				

			}
		}
		
		
		return toReturn;
	}
	
	//TODO continue here.
	public void PrintPossibilityTree(int depth, GameState game){
		if(depth > 0){
			TreeNode root = GetOutComes(depth, game);
			root.PrintMe();
			System.out.println("\n");
			ArrayList<TreeNode> children = root.children;
			
			while(!children.isEmpty()){
				ArrayList<TreeNode> next = new ArrayList<TreeNode>();
				ArrayList<TreeNode> temp = new ArrayList<TreeNode>();
				for (int i = 0; i < children.size(); i ++){
					children.get(i).PrintMe();
					temp = children.get(i).children;
					for (int j = 0; j < temp.size(); j ++){
						next.add(temp.get(j));
					}
				}	
				children = next;
				System.out.println("\n");
			}
			
		}
		else{
			System.out.println("Enter a depth greater than 0 or you will be destroyed!!!!");
		}
	}
	
}
