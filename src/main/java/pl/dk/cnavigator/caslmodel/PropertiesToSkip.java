package pl.dk.cnavigator.caslmodel;

import java.util.List;

import static java.util.Arrays.asList;

class PropertiesToSkip {

    static List<String> propertiesToSkip = asList("_id", "mtvi:created", "mtvi:createdBy", "mtvi:publishedTo",
            "mtvi:publishedToBy", "mtvi:publishedFromEnvironment", "mtvi:schemaValidFor", "mtvi:updatedBy", "mtvi:updated",
            "_changeDates", "_lm", "mtvi:lastModified", "mtvi:minor", "mtvi:minorOriginal", "ModificationReason");

    static boolean shouldSkip(String property) {
        return propertiesToSkip.contains(property);
    }

    static boolean shouldNotSkip(String property) {
        return !shouldSkip(property);
    }

}
