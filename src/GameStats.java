
public class GameStats {
		public int won;
		public int myPoints;
		public int opponentsPoints;
		public int rivalIndex;
		
		public GameStats(int wonP, int mPoints, int opPoints, int rival){
			won = wonP;
			myPoints = mPoints;
			opponentsPoints = opPoints;
			rivalIndex = rival;
		}
		
		public GameStats InverseValues(){
			return new GameStats(this.won * -1, this.opponentsPoints, this.myPoints, this.rivalIndex);
		}
}
