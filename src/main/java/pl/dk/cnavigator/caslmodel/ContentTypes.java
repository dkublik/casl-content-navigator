package pl.dk.cnavigator.caslmodel;

import java.util.List;

import static java.util.Arrays.asList;

public class ContentTypes {

    public static final String MUSIC_VIDEO = "Standard:MusicVideo";

    public static final String ISRC_DIST_POLICY = "Standard:ISRCDistPolicy";

    public static final String DIST_POLICY = "Standard:DistPolicy";

    public static final String MUSIC_GENRE = "Standard:MusicGenre";

    public static final String MUSIC_VIDEO_CMR = "Standard:MusicVideoCMR";

    public static final String VIDEO_ASSET_REF = "Standard:VideoAssetRef";

    public static final String IMAGE = "Standard:Image";

    public static final String IMAGE_CMR = "Standard:ImageCMR";

    public static final String IMAGE_ASSET_REF = "Standard:ImageAssetRef";

    public static final String IMAGE_TYPE = "Standard:ImageType";

    public static final String PERSON_CONTEXT = "Standard:PersonContext";

    public static final String PERSON = "Standard:Person";

    public static final String STANDARDS = "Standard:Standards";

    public static final String RATING_STANDARDS = "Standard:RatingStandards";

    public static final String GOVERNING_AGREEMENT = "Standard:GoverningAgreement";

    public static final String PLATFORM_BUCKET = "Standard:PlatformBucket";

    public static final String REGION_BUCKET = "Standard:RegionBucket";

    public static final String ORGANIZATION_BUCKET = "Standard:OrganizationBucket";

    public static final String AUTHORIZATION_BUCKET = "Standard:AuthorizationBucket";

    public static final List<String> DP_BUCKET_TYPES = asList(PLATFORM_BUCKET, REGION_BUCKET, ORGANIZATION_BUCKET, AUTHORIZATION_BUCKET);

}
