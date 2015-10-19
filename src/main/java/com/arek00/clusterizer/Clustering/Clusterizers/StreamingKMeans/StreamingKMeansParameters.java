package com.arek00.clusterizer.Clustering.Clusterizers.StreamingKMeans;

import com.arek00.clusterizer.validators.NumberValidator;
import lombok.Getter;
import lombok.NonNull;
import org.apache.mahout.clustering.streaming.mapreduce.StreamingKMeansDriver;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.math.neighborhood.ProjectionSearch;
import org.apache.mahout.math.neighborhood.Searcher;

public class StreamingKMeansParameters {

    public static String MAPREDUCE_METHOD = "mapreduce";
    public static String SEQUENTIAL_METHOD = "sequential";

    @Getter private int numClusters;
    @Getter private int estimatedNumMapClusters;
    @Getter private float estimatedDistanceCutOff;
    @Getter private int maxNumIteration;
    @Getter private float trimFraction;
    @Getter private boolean randomInit;
    @Getter private boolean ignoreWeights;
    @Getter private float testProbability;
    @Getter private int numBallKMeans;
    @Getter private String measureClass;
    @Getter private String searcherClass;
    @Getter private int searchSize;
    @Getter private int numProjections;
    @Getter private String method;
    @Getter private boolean reduceStreamingKMeans;

    private StreamingKMeansParameters(int numCluster, int estimatedNumMapClusters, float estimatedDistanceCutOff,
                                      int maxNumIteration, float trimFraction, boolean randomInit, boolean ignoreWeights,
                                      float testProbability, int numBallKMeans, String measureClass, String searcherClass,
                                      int searchSize, int numProjections, String method, boolean reduceStreamingKMeans) {
        this.numClusters = numCluster;
        this.estimatedNumMapClusters = estimatedNumMapClusters;
        this.estimatedDistanceCutOff = estimatedDistanceCutOff;
        this.maxNumIteration = maxNumIteration;
        this.trimFraction = trimFraction;
        this.randomInit = randomInit;
        this.ignoreWeights = ignoreWeights;
        this.testProbability = testProbability;
        this.numBallKMeans = numBallKMeans;
        this.measureClass = measureClass;
        this.searcherClass = searcherClass;
        this.searchSize = searchSize;
        this.numProjections = numProjections;
        this.method = method;
        this.reduceStreamingKMeans = reduceStreamingKMeans;
    }


    public static class Builder {

        private int numClusters = 2;
        private int estimatedNumMapClusters = numClusters + 1;
        private float estimatedDistanceCutOff = 1.0f / numClusters;
        private int maxNumIteration = 1;
        private float trimFraction = 1f;
        private boolean randomInit = false;
        private boolean ignoreWeights = true;
        private float testProbability = 0.5f;
        private int numBallKMeans = 4;
        private String measureClass = SquaredEuclideanDistanceMeasure.class.getCanonicalName();
        private String searcherClass = ProjectionSearch.class.getCanonicalName();
        private int searchSize = 1;
        private int numProjections = 1;
        private String method = StreamingKMeansParameters.MAPREDUCE_METHOD;
        private boolean reduceStreamingKMeans = false;


        /**
         * Set demanded number of clusters to create.
         * Note that algorithm not necessarily create exact number as passed in parameter.
         * Number of optimal created clusters may be only close to this parameter.
         * <p>
         * Value greater than 1.
         *
         * @param clustersNumber
         * @return
         */
        public Builder clustersNumber(int clustersNumber) {
            NumberValidator.greaterThan("Clusters Number has to be greater than 1", 1, clustersNumber);

            this.numClusters = clustersNumber;

            this.estimatedDistanceCutOff = (this.estimatedDistanceCutOff != -1) ? 1.0f / numClusters : this.estimatedDistanceCutOff;

            this.estimatedNumMapClusters = clustersNumber + 1;
            return this;
        }


        /**
         * Value of distance between point and closest centroid after which point will
         * be definitively assigned to new cluster.
         * <p>
         * Default value equals 1.0/clustersNumber,
         * set this value AFTER set clusters number to affect this value.
         * Set value of StreamingKMeansDriver.INVALID_DISTANCE_CUTOFF to ignore this.
         *
         * @param estimatedDistanceCutOff
         * @return
         */
        public Builder distanceCutoff(float estimatedDistanceCutOff) {
            if (estimatedDistanceCutOff < 0) {
                NumberValidator.equals("Estimated Distance Cut Off has to be equal -1 or greater than 0. " +
                                "Use StreamingKMeansDriver.INVALID_DISTANCE_CUTOFF if you want to ignore cut offs",
                        StreamingKMeansDriver.INVALID_DISTANCE_CUTOFF, estimatedDistanceCutOff);
            }

            this.estimatedDistanceCutOff = estimatedDistanceCutOff;
            return this;
        }

        /**
         * Number of clusters requested from each mapper.
         * Value has to be greater than clustersNumber parameter so set it
         * before number of clusters.
         *
         * @param numberOfMappedClusters
         * @return
         */

        public Builder estimatedNumberMappedCluster(int numberOfMappedClusters) {
            NumberValidator.greaterThan("Estimated number of mapped clusters has to be greater than " +
                    "clusters number value which is currently equals " + numClusters, numClusters, numberOfMappedClusters);

            this.estimatedNumMapClusters = numberOfMappedClusters;
            return this;
        }

        /**
         * Set number of possible maximum iteration that algorithm can take.
         * Value has to be greater than 0.
         *
         * @param maxIterations
         * @return
         */
        public Builder maxIterations(int maxIterations) {
            NumberValidator.greaterThan("Set number of iterations greater than 0", 0, maxIterations);

            this.maxNumIteration = maxIterations;
            return this;
        }

        /**
         * The fraction of the points to be considered in updating a ball k-means
         * Value greater than 0.
         *
         * @return
         */
        public Builder trimFraction(float trimFraction) {
            NumberValidator.greaterThan("Set trim fraction value greater than 0", 0, trimFraction);

            this.trimFraction = trimFraction;
            return this;
        }

        /**
         * Set true if ball's kmeans should be initialized randomly.
         * Default false.
         *
         * @param randomInit
         * @return
         */
        public Builder randomInit(boolean randomInit) {
            this.randomInit = randomInit;
            return this;
        }

        /**
         * Ignore invalid final ball kmeans weights
         * Default true
         *
         * @param ignoreWeights
         * @return
         */
        public Builder ignoreWeights(boolean ignoreWeights) {
            this.ignoreWeights = ignoreWeights;
            return this;
        }

        /**
         * Set percentage of tested vectors selected to determine best final centroids
         * Value in range [0;1)
         * Default is 0.5
         *
         * @param percentage
         * @return
         */
        public Builder testProbabilityPercentage(float percentage) {
            NumberValidator.greaterOrEqual("Number has to be greater or equal 0", 0f, percentage);
            NumberValidator.lesserThan("Number has to be lower than 1", 1f, percentage);

            this.testProbability = percentage;
            return this;
        }

        /**
         * The number of BallKMeans runs in the reducer that determine the centroids to return
         * (clusters are computed for the training set and the error is computed on the test set)
         * Value greater than 0
         * Default is 4
         */
        public Builder numberOfBallKMeans(int numberOfBallKMeans) {
            NumberValidator.greaterThan("Number of ball KMeans has to be greater than 0", 0, numberOfBallKMeans);
            this.numBallKMeans = numberOfBallKMeans;
            return this;
        }

        /**
         * Class to measure distance.
         * Default is SquaredEuclideanDistanceMeasure.
         */
        public Builder measureClass(@NonNull Class<? extends DistanceMeasure> measureClass) {
            this.measureClass = measureClass.getCanonicalName();
            return this;
        }

        /**
         * Searcher to find neighboorhood.
         * Default is ProjectionSearch
         *
         * @param searcher
         * @return
         */
        public Builder searcherClass(@NonNull Class<? extends Searcher> searcher) {
            this.searcherClass = searcher.getCanonicalName();
            return this;
        }

        /**
         * The number of closest neighbors to look at for selecting the closest one in approximate nearest
         * neighbor searches
         *
         * @param searchSize
         * @return
         */
        public Builder searchSize(int searchSize) {
            NumberValidator.greaterThan("Set search size greater than 0", 0, searchSize);

            this.searchSize = searchSize;
            return this;
        }

        /**
         * Number of projected vectors used to faster searching.
         * Useful only for ProjectionSearch and FastProjectionSearch.
         * Default is 1.
         *
         * @param projections
         * @return
         */
        public Builder numberOfProjections(int projections) {
            NumberValidator.greaterThan("Set number of projections greater than 0", 0, projections);

            this.numProjections = projections;
            return this;
        }

        /**
         * Method of execution clustering task.
         * Choose from:
         * - StreamingKMeansParameters.MAPREDUCE_METHOD,
         * - StreamingKMeansParameters.SEQUENTIAL_METHOD.
         * <p>
         * Default value is: StreamingKMeansParameters.MAPREDUCE_METHOD.
         *
         * @param method
         * @return
         */
        public Builder taskExecutionMethod(@NonNull String method) {
            if (method.equals(StreamingKMeansParameters.MAPREDUCE_METHOD) ||
                    method.equals(StreamingKMeansParameters.SEQUENTIAL_METHOD)) {
                this.method = method;
            } else {
                throw new IllegalArgumentException("Set correct execution method");
            }

            return this;
        }

        /**
         * There might be too many intermediate clusters from the mapper to fit into memory,
         * so the reducer can run another pass of StreamingKMeans
         * to collapse them down to a fewer clusters.
         * <p>
         * Default is false;
         *
         * @param reduceStreamingKMeans
         * @return
         */

        public Builder reduceStreamingKMeans(boolean reduceStreamingKMeans) {
            this.reduceStreamingKMeans = reduceStreamingKMeans;
            return this;
        }

        public StreamingKMeansParameters build() {
            return new StreamingKMeansParameters(numClusters, estimatedNumMapClusters, estimatedDistanceCutOff,
                    maxNumIteration, trimFraction, randomInit, ignoreWeights, testProbability, numBallKMeans,
                    measureClass, searcherClass, searchSize, numProjections, method, reduceStreamingKMeans);
        }

    }

}
