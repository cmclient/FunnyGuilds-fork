package net.dzikoysk.funnyguilds.rank;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.dzikoysk.funnyguilds.config.NumberRange;
import net.dzikoysk.funnyguilds.config.PluginConfiguration;

import java.util.Map;
import java.util.function.BiFunction;

public final class RankSystem {

    private final Map<Type, RankingAlgorithm> map;

    private RankSystem(Map<Type, RankingAlgorithm> map) {
        this.map = map;
    }

    public RankResult calculate(Type type, int attackerPoints, int victimPoints) {
        return this.map.get(type).apply(attackerPoints, victimPoints);
    }

    public static RankSystem create(PluginConfiguration config) {
        ImmutableMap<Type, RankingAlgorithm> build = new ImmutableMap.Builder<Type, RankingAlgorithm>()
                .put(Type.ELO, (attackerPoints, victimPoints) -> {
                    int attackerElo = NumberRange.inRange(attackerPoints, config.eloConstants).orElseGet(0);
                    int victimElo = NumberRange.inRange(victimPoints, config.eloConstants).orElseGet(0);

                    double attackerE = 1.0D / (1.0D + Math.pow(config.eloExponent, (victimPoints - attackerPoints) / config.eloDivider));
                    double victimE = 1.0D / (1.0D + Math.pow(config.eloExponent, (attackerPoints - victimPoints) / config.eloDivider));

                    attackerElo = (int) Math.round(attackerElo * (1 - attackerE));
                    victimElo = (int) Math.round(victimElo * (0 - victimE) * -1);

                    return new RankResult(attackerElo, victimElo);
                })
                .put(Type.PERCENT, (attackerPoints, victimPoints) -> new RankResult((int) (victimPoints * (config.percentRankChange / 100.0))))
                .put(Type.STATIC, (attackerPoints, victimPoints) -> new RankResult(config.staticAttackerChange, config.staticVictimChange))
                .put(Type.DYNAMIC, (attackerPoints, victimPoints) -> {
                    // Calculate the absolute difference between attacker and victim points
                    int pointDifference = Math.abs(attackerPoints - victimPoints);

                    // Set the default rank change to 20 points
                    int defaultRankChange = 20;

                    // Calculate the rank change based on the point difference
                    int rankChange;

                    if (victimPoints > attackerPoints) {
                        // If victim has more points, increase rank change
                        rankChange = Math.min(150, defaultRankChange + (pointDifference / 30));
                    } else {
                        // If attacker has more points or points are equal, decrease rank change
                        rankChange = Math.max(2, defaultRankChange - (pointDifference / 100));
                    }

                    // Create a RankResult and return it
                    return new RankResult(rankChange, rankChange / 2);
                })
                .build();

        return new RankSystem(Maps.newEnumMap(build));
    }

    public enum Type {

        ELO,
        PERCENT,
        STATIC,
        DYNAMIC

    }

    public static class RankResult {

        private final int attackerPoints;
        private final int victimPoints;

        public RankResult(int attackerPoints, int victimPoints) {
            this.attackerPoints = attackerPoints;
            this.victimPoints = victimPoints;
        }

        public RankResult(int samePoints) {
            this.attackerPoints = samePoints;
            this.victimPoints = samePoints;
        }

        public int getAttackerPoints() {
            return this.attackerPoints;
        }

        public int getVictimPoints() {
            return this.victimPoints;
        }

    }

    public interface RankingAlgorithm extends BiFunction<Integer, Integer, RankResult> {
    }

}
