
public class smartTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		playerAphaBetaTest p1 =  new playerAphaBetaTest();
		GameState game = new GameState();
		p1.PrintPossibilityTree(3, game);
	}

}
