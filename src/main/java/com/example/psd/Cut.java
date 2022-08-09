package com.example.psd;

/**
 * an enum to specify how the plan is cut
 */
public enum Cut {
    Vertical {
        public String toString() {
            return "|";
        }
    },
    Horizontal {
        public String toString() {
            return "-";
        }
    },
    Leaf {
        public String toString() {
            return "Leaf";
        }
    }
}
