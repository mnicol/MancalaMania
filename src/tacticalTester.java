
public class tacticalTester {

	
	 /** time limit each player has to decide on a move (default: 10 secs) **/
    public static final int TIME_LIMIT  = 10;

    /** number of stones with which to initialize bins (default: 4) **/
    public static final int NUM_STONES = 4;
    
    /** pausing time when moving stones among pots **/
    public static final int SLEEP_TIME = 400;

    /**this is the gui for the game **/
    
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		GameState game = new GameState();
		System.out.println("Welcome to insanity.");
		testPlayer1 p1 = new testPlayer1();
    	player2 p2 = new player2();
    	int maxTimesWon = 0;
    	int [] bestWeight = {0, 0, 0, 0, 0, 0, 0};
    	for(int a =0; a < 10; a++){
    		for(int b =0; b < 10; b++){
    			for(int c =0; c < 10; c++){
    				for(int d =0; d < 10; d++){
    					for(int e =0; e < 10; e++){
    						for(int f =0; f < 10; f++){
    							for(int g =0; g < 10; g++){
    								int[] p1W = {a, b, c, d, e, f, g};
    				        		p1.SetWeights(p1W);
    								int tempWins = 0;
    								
    								for(int h =0; h < 10; h++){
    						    		for(int i =0; i < 10;i++){
    						    			for(int j =0; j < 10; j++){
    						    				for(int k =0; k < 10; k++){
    						    					for(int l =0; l < 10; l++){
    						    						for(int m =0; m < 10; m++){
    						    							for(int n =0; n < 10; n++){
    						    								int[] p2W = {h, i, j, k, l, m, n};
    						    								p2.SetWeights(p2W);
    						    				        		while(!game.gameOver()){
    						    				        			p1.move(game);
    						    				        			while(game.applyMove(p1.move)){
    						    				        				p1.move(game);
    						    				        			}
    						    				        			if (!game.gameOver()){
    						    				        				p2.move(game);
    						    				        				while(game.applyMove(p2.move)){
    						    				        					p2.move(game);
    						    				  
    						    				        				}
    						    				        			}
    						    				        			
    						    				        		}
    						    				        		if (game.status() == 1){
    						    				        			tempWins++;
    						    				        		}
    						    				        		
    						    				        		
    						    				        	}
    						    			        	}
    						    		        	}
    						    	        	}
    						            	}
    						        	}
    						    	}
    								
    								if(tempWins > maxTimesWon){
    									bestWeight = p1W;
    									maxTimesWon = tempWins;
    								}
    								System.out.println(maxTimesWon);
    				        	}
    			        	}
    		        	}
    	        	}
            	}
        	}
    	}
    	
    	System.out.println("Max wins: " + maxTimesWon);
    	
    	System.out.println("Best weight");
    	for (int i = 0; i < bestWeight.length; i ++){
    		System.out.println(bestWeight[i]);
    	}
    	//set up the gui
    	
    	
    	
    	

    	
    	
    	/**Set up the gui**/
    	
    	
    	

	}

}
