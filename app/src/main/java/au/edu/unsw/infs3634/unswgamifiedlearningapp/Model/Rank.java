package au.edu.unsw.infs3634.unswgamifiedlearningapp.Model;

public class Rank {
    private int score;
    private int rank;

    public Rank(int score, int rank) {
        this.score = score;
        this.rank = rank;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

}
