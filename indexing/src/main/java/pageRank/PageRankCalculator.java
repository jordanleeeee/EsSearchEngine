package pageRank;

import es.searchResponse.SearchResponse;
import util.MatrixUtils;
import util.WebsiteUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * @author Jordan
 */
public class PageRankCalculator {
    private final Map<String, Integer> pageIdToIndexMap;
    private final double[][] pageLinkMatrix;
    private double[] pageRankVector;
    private final double dampingFactor;
    private final int iteration;
    private final int normalizationMode;

    /**
     * normalizationMode = 0, 1, 2
     * 0: no normalization at all
     * 1: normalize all PR values in every iteration with L1 norm
     * 2: normalize all PR values with L1 norm only at the end of final iteration
     */
    public PageRankCalculator(SearchResponse searchResponse, int iteration, double dampingFactor, int normalizationMode) {
        this.iteration = iteration;
        this.dampingFactor = dampingFactor;
        this.normalizationMode = normalizationMode;
        pageIdToIndexMap = new HashMap<>();

        int nextIndex = 0;
        for (SearchResponse.HitDetails hitDetail : searchResponse.hits.hitDetails) {
            pageIdToIndexMap.put(hitDetail._id, nextIndex++);
        }

        System.out.println(nextIndex);
        pageLinkMatrix = new double[nextIndex][nextIndex];
        for (SearchResponse.HitDetails hitDetail : searchResponse.hits.hitDetails) {
            int parentIdx = getIndexOfUrl(hitDetail.source.url).orElseThrow();
            for (String link : hitDetail.source.links) {
                OptionalInt childIdx = getIndexOfUrl(link);
                if (childIdx.isEmpty()) continue;
                pageLinkMatrix[parentIdx][childIdx.getAsInt()] = 1;
            }
        }

        pageRankVector = new double[nextIndex];
        Arrays.fill(pageRankVector, 1);
    }

    private OptionalInt getIndexOfUrl(String url) {
        String id = WebsiteUtils.getUrlId(WebsiteUtils.simplifyUrl(url));
        if (pageIdToIndexMap.containsKey(id)) {
            return OptionalInt.of(pageIdToIndexMap.get(id));
        }
        return OptionalInt.empty();
    }

    public void calculatePageRank() {
        MatrixUtils.normalizeMatrixRow(pageLinkMatrix);
        for (int i = 0; i < iteration; i++) {
            System.out.println(i + " iteration");
            double[] newPageRankVector = MatrixUtils.vectorTimesMatrix(pageRankVector, pageLinkMatrix);

            MatrixUtils.multiplyValueToVector(dampingFactor, newPageRankVector);
            MatrixUtils.addValueToVector((1 - dampingFactor), newPageRankVector);
            if (normalizationMode == 1 || normalizationMode == 2 && i == iteration - 1) {
                MatrixUtils.normalizeVector(newPageRankVector);
            }
            pageRankVector = newPageRankVector;
            System.out.println(Arrays.toString(pageRankVector));
        }
    }

    public void showSortedPageRankMap(int top) {
        Map<String, Double> scoreMap = pageIdToIndexMap
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), pageRankVector[entry.getValue()]))
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(top)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
        System.out.println(scoreMap);
    }

    public Map<String, Double> getPageRankMap() {
        return pageIdToIndexMap
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), round(pageRankVector[entry.getValue()])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * round to 8 decimal place (at most 9 significant bits for the precision in es rank_feature)
     */
    private double round(double value) {
        int place = 8;
        long factor = (long) Math.pow(10, place);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}