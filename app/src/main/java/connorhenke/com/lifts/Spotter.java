package connorhenke.com.lifts;

public class Spotter {

    public static double estimateOneRepMax(int reps, double weight) {
        return weight / (1.0278f - 0.0278f * reps);
    }

    public static double getWeightFromRepsAndMax(int reps, double max) {
        return max * (1.0278f - 0.0278f * reps);
    }

    public static double estimateRepMax(int fromReps, double weight, int resultReps) {
        double oneRep = estimateOneRepMax(fromReps, weight);
        return getWeightFromRepsAndMax(resultReps, oneRep);
    }
}
