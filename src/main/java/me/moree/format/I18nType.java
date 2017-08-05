package me.moree.format;

public enum I18nType {
    EN,
    ZH;

    @Override
    public String toString() {
        switch (this) {
            case EN: return "en";
            case ZH: return "zh";
            default: throw new IllegalStateException("Unknown I18n Type, check i18n.internal.Type.toString()");
        }
    }

    public static I18nType asType(String typeName) {
        if(typeName == null) return EN;
        String s = typeName.toLowerCase();
        if (s.equals("en")) {
            return EN;
        } else if (s.equals("zh")) {
            return ZH;
        }

        return EN;
    }
}
