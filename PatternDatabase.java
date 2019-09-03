/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sokoban;

import java.util.*;
import java.util.List;

/**
 * @author steven
 */
public class PatternDatabase {

    GameState st;
    HashMap<String, Integer> hm = new HashMap<>();


    public PatternDatabase(GameState st) {
        this.st = st;
        constructDB();
    }


    //This is the method which you need to implement
    public int getEstimatedDistanceToGoal(List<Position> positions) {

        int sum = 0;
        for (Position blockPosition : positions) {
            int count = hm.get(blockPosition.toString());
            if (count == Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            sum += count;

        }

        return sum;
    }


    public ArrayList<Position> getAvaliablePositions() {

        ArrayList<Position> notWallPos = new ArrayList();
        for (int i = 0; i < st.getMaxX(); i++) {
            for (int j = 0; j < st.getMaxY(); j++) {
                if (st.getTypes()[i][j] != 'w') {
                    notWallPos.add(new Position(i, j));
                }
            }
        }

        return notWallPos;
    }

    public ArrayList<Position> playerPossPos(int x, int y) {

        int xCor;
        int yCor;

        ArrayList<Position> possPlayPos = new ArrayList();

        if (x + 1 < st.getMaxX() && x + 1 > 0 && y < st.getMaxY() && y > 0 && st.getTypes()[x + 1][y] != 'w') {
            xCor = x + 1;
            yCor = y;
            possPlayPos.add(new Position(xCor, yCor));

        }
        if (x - 1 < st.getMaxX() && x - 1 > 0 && y < st.getMaxY() && y > 0 && st.getTypes()[x - 1][y] != 'w') {
            xCor = x - 1;
            yCor = y;
            possPlayPos.add(new Position(xCor, yCor));

        }
        if (x < st.getMaxX() && x > 0 && y + 1 < st.getMaxY() && y + 1 > 0 && st.getTypes()[x][y + 1] != 'w') {
            xCor = x;
            yCor = y + 1;
            possPlayPos.add(new Position(xCor, yCor));

        }
        if (x < st.getMaxX() && x > 0 && y - 1 < st.getMaxY() && y - 1 > 0 && st.getTypes()[x][y - 1] != 'w') {
            xCor = x;
            yCor = y - 1;
            possPlayPos.add(new Position(xCor, yCor));

        }
        return possPlayPos;
    }

    public void constructDB() {

        ArrayList<Position> positionList = getAvaliablePositions();

        GameState copyGameState = new GameState(st);

        for (Position position : positionList) {

            int min = Integer.MAX_VALUE;
            ArrayList<Position> possPlayPos = playerPossPos(position.getX(), position.getY());

            for (Position playerPossPos : possPlayPos) {
                try {
                    copyGameState.setRelaxedState(position.getX(), position.getY(), playerPossPos.getX(), playerPossPos.getY());
                    SimpleSokobanAstarPlayer player = new SimpleSokobanAstarPlayer(copyGameState, false);
                    List<State> distanceLen = player.findPathToGoal();
                    if (distanceLen != null) {
                        int distance = distanceLen.size() - 1;
                        min = Math.min(min, distance);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            hm.put(position.toString(), min);
        }
    }
}



