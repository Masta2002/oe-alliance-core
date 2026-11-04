SUMMARY = "exteplayer3 - media player for E2"
DESCRIPTION = "Core of movie player for E2 based on the libeplayer using the ffmpeg solution"
SECTION = "multimedia"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

# The system FFmpeg 9 carries the playback patches of ffmpeg-ext (see
# recipes-multimedia/ffmpeg). OpenBH and OpenViX build their own exteplayer3,
# which still needs FFmpeg 6.1: they stay on ffmpeg-ext.
EXTEPLAYER3_FFMPEG = "ffmpeg"
EXTEPLAYER3_FFMPEG:openbh = "ffmpeg-ext"
EXTEPLAYER3_FFMPEG:openvix = "ffmpeg-ext"

DEPENDS = "${EXTEPLAYER3_FFMPEG} zlib bzip2 libxml2 xz libbluray openssl librtmp libudfread"
RDEPENDS:${PN} += "libxml2 zlib bzip2 liblzma libbluray openssl librtmp libudfread"
RDEPENDS:${PN}:append:openbh = " ffmpeg-ext-libs"
RDEPENDS:${PN}:append:openvix = " ffmpeg-ext-libs"

inherit gitpkgv autotools pkgconfig upx-compress

SRCREV = "${AUTOREV}"
PV = "183+git"
PKGV = "183+git${GITPKGV}"

SRC_URI:openbh = "git://github.com/BlackHole/exteplayer3.git;branch=master;protocol=https"
SRC_URI:openvix = "git://github.com/OpenViX/exteplayer3.git;branch=master;protocol=https"
SRC_URI = "git://github.com/oe-alliance/exteplayer3.git;branch=master;protocol=https"

EXTRA_OECONF = ""

PACKAGECONFIG ??= ""
PACKAGECONFIG:append:dreamone = " dreamnextgen"
PACKAGECONFIG:append:dreamtwo = " dreamnextgen"
PACKAGECONFIG[dreamnextgen] = "--enable-dreamnextgen,--disable-dreamnextgen,alsa-lib"

# ffmpeg-ext lives in its own directory, with its own pkg-config dir
FFMPEG_EXT_LDFLAGS = " \
    -L${STAGING_LIBDIR}/ffmpeg-ext \
    -Wl,-rpath,/usr/lib/ffmpeg-ext \
    -Wl,-rpath-link,${STAGING_LIBDIR}/ffmpeg-ext \
"
LDFLAGS:append:openbh = "${FFMPEG_EXT_LDFLAGS}"
LDFLAGS:append:openvix = "${FFMPEG_EXT_LDFLAGS}"

do_configure:prepend:openbh() {
    export PKG_CONFIG_PATH="${STAGING_LIBDIR}/ffmpeg-ext/pkgconfig:${PKG_CONFIG_PATH}"
}
do_configure:prepend:openvix() {
    export PKG_CONFIG_PATH="${STAGING_LIBDIR}/ffmpeg-ext/pkgconfig:${PKG_CONFIG_PATH}"
}

INSANE_SKIP:${PN}:append:openbh = " ldflags rpaths"
INSANE_SKIP:${PN}:append:openvix = " ldflags rpaths"
