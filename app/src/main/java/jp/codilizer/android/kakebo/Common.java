package jp.codilizer.android.kakebo;

/**
 * Created by codilizer on 2016/04/30.
 */
public class Common {
    public enum AccountDivision {
        income(0), spending(1);


        private final int value;
        private AccountDivision(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static AccountDivision getDivision(int val){
            switch(val) {
                case 0:
                    return income;
                case 1:
                    return spending;
            }
            return null;
        }
    }


}
