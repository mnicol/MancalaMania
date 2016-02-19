import java.util.ArrayList;
import java.util.Random;


public class StandardFighters extends BasePlayer {

	public enum FighterType{
		MustBeat,
		BattleHardenedBeast,
		RandomFighters,
	}
	public int GamesWon;
	public int GamesLost;
	public int GamesTied;
	public FighterType type;
	public ArrayList<GameStats> gameStats;
	public StandardFighters(FighterType typeP ){
		gameStats = new ArrayList<>();
		type = typeP;
	}
	
	public StandardFighters(){
		gameStats = new ArrayList<>();
		this.type = FighterType.RandomFighters;
	}
	private static Random  rand = new Random();
	public void CreateRandomWeights(){
		for(int i = 0; i < TestSBE.NumWeights; i++){
			this.Weights[i] = rand.nextDouble() + rand.nextInt(5);
		}
	}
	
	
	
}
