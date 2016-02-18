public class TestSBE {

	GameState state = null;
	
	public TestSBE(GameState state)
	{
		this.state = state;
	}
	
	public TestSBE()
	{
		this.state = null;
	}
	
	public void setState(GameState state)
	{
		this.state = state;
	}
	
	public GameState getState()
	{
		return this.state;
	}
	
	// Run multiple times slightly changing weights and see how the outcome comes out
	
	/**
	 * Gives you the SBE value of the current board and prints off the 
	 * 	SBE value of all the possible moves from that position
	 * @param state
	 * @return
	 */
	public int testSBE(GameState state)
	{	
		// Get moves for playerA
		System.out.println("**************************");
		System.out.println("*     Player A (MAX)     *");
		System.out.println("**************************");
		System.out.println();
		for(int i = 0; i <= 5; i++)
		{
			if(state.stoneCount(i) > 0)
			{
				GameState temp = new GameState(state);
				temp.applyMove(i);
				
				System.out.println("Move: " + i);
				printGameState(temp);
				System.out.println("SBE: " + sbe(temp));
				System.out.println();
			}
		}
		
		// Get Moves for playerB
		System.out.println();
		System.out.println("**************************");
		System.out.println("*     Player B (MIN)     *");
		System.out.println("**************************");
		System.out.println();
		for(int i = 7; i <= 12; i++)
		{
			if(state.stoneCount(i) > 0)
			{
				GameState temp = new GameState(state);
				temp.applyMove(i);
				
				System.out.println("Move: " + i);
				printGameState(temp);
				System.out.println("SBE: " + sbe(temp));
				System.out.println();
			}
		}
		
		return sbe(state);
	}
	
	private static void printGameState(GameState state)
	{
		System.out.println(state.toString());
	}
	
	//the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
    private static int sbe(GameState state)
    {
    	int SbeValue = 0;
    	
    	// Add advantage from being able to land in your mancala
    	SbeValue += landInMancala(state, 2.0);
    	
    	//Checks to see if there is one stone two bins away from mancala
    	SbeValue +=  checkTwoOut(state, 1.0);
    	
    	// Add advantage from not having pieces in your bin before mancala
    	SbeValue += emptyPreMancala(state, 1.0);
    	
    	// Add advantage of potentially being able to steal beans
    	SbeValue += stealPotential(state, 2.0);
    	
    	// Add advantage from having more pieces
    	SbeValue += pieceDistribution(state, 2.0);
    	
    	// Determine what player has placed more stones in their mancala
    	SbeValue += pointDifferental(state, 1.0);
    	
    	// Determine if move will secure victory
    	SbeValue += halfInBin(state);
    	
    	//Single Bin Functions
    	
    	// Calculate value from difference in first bin
    	//SbeValue += firstBin(state, 1.0);
    	
    	// Calculate value from difference in first bin
    	//SbeValue += secondBin(state, 1.0);
    	
    	// Calculate value from difference in first bin
    	//SbeValue += thirdBin(state, 1.0);
    	
    	// Calculate value from difference in first bin
    	//SbeValue += fourthBin(state, 1.0);
    	
    	// Calculate value from difference in first bin
    	//SbeValue += fifthBin(state, 1.0);
    	
    	// Calculate value from difference in first bin
    	//SbeValue += sixthBin(state, 1.0);
    	
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
