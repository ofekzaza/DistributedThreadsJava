package Master;

public enum CloseType {
    Kill{
        @Override
        public String toString() {
            return "kill";
        }
    }, PreForNext{
        @Override
        public String toString() {
            return "prepare";
        }
    };
}
