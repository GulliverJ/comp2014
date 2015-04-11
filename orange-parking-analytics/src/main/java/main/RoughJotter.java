package main;

public class RoughJotter {

	//I had some code from the API that was better used here so will be using this class to just throw rough bits in for later use
	
	/*
	public int findNextClosest(int id, int[] ids) {
		double tempDist;
		double compDist = 999;
		int closest = id;
		Position[] searchPos = this.getPositionList(ids);
		Position rootPos = this.getPosition(id);
		for(int i = 0; i < ids.length; i++) {
			tempDist = getDistanceBetween(rootPos, searchPos[i]);
			if(tempDist > compDist) {
				continue;
			} else {
				compDist = tempDist;
				closest = searchPos[i].getID();
			}
		}
		return closest;
	}
	
	public static double getDistanceBetween(Position pos1, Position pos2) {
		double x = Math.abs(pos1.getLng() - pos2.getLng());
		double y = Math.abs(pos1.getLat() - pos2.getLat());
		return Math.sqrt(x*x + y*y);
	}
	*/
}
