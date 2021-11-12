package au.edu.unsw.infs3634.unswgamifiedlearningapp;

public class TestQuestion {
    private String testID;
    private int topScore;
    private int time;

    public TestQuestion(String testID, int topScore, int time) {
        this.testID = testID;
        this.topScore = topScore;
        this.time = time;
    }

    public String getTestID() {
        return testID;
    }

    public int getTopScore() {
        return topScore;
    }

    public int getTime() {
        return time;
    }
}
