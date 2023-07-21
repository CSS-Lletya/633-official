package skills.thieving.impl.sorceresssgarden;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SqirkFruitSqueeze {

	@AllArgsConstructor
    public enum SqirkFruit {
        WINTER(10847, 5, 10851),
        SPRING(10844, 4, 10848),
        AUTUMM(10846, 3, 10850),
        SUMMER(10845, 2, 10849);

    	@Getter
        private int fruitId, amtRequired, resultId;
    }
}