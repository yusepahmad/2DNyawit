package com.uph_lpjk.sawit2d.farm;

public class GardenerService {
    private static final int HARVEST_HELP_COST = 65;
    private static final double SALE_DISCOUNT = 0.6;

    public GardenerResult prepareHarvest(FarmGrid grid) {
        int ready = grid.countReadyTiles();
        if (ready == 0) {
            return new GardenerResult(0, 0, "Tidak ada sawit siap panen untuk dibantu.");
        }
        return new GardenerResult(
                ready,
                HARVEST_HELP_COST,
                "Tukang kebun siap memanen "
                        + ready
                        + " tile (biaya "
                        + HARVEST_HELP_COST
                        + " gold).");
    }

    public GardenerSaleResult sellWithDiscount(
            int stock, EconomySystem economy, double weatherMultiplier) {
        if (stock == 0) {
            return new GardenerSaleResult(
                    0, 0, SALE_DISCOUNT, "Tidak ada stok untuk dijual via tukang kebun.");
        }
        double appliedMultiplier = SALE_DISCOUNT * weatherMultiplier;
        int discountedIncome = economy.collectIncome(stock, appliedMultiplier);
        return new GardenerSaleResult(
                stock,
                discountedIncome,
                appliedMultiplier,
                "Tukang kebun menjual "
                        + stock
                        + " unit dengan multiplier "
                        + Math.round(appliedMultiplier * 100)
                        + "% (+"
                        + discountedIncome
                        + " gold).");
    }
}
