package com.example.psd;

public enum Direction {
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
            return "P";
        }
    }
}
