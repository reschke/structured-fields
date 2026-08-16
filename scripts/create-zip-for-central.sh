#!/usr/bin/env bash
set -euo pipefail

#
# Build a Maven Central bundle ZIP from already-built artifacts.
#
# Expected files in target/:
#
#   structured-fields-0.6.jar
#   structured-fields-0.6.jar.asc
#   structured-fields-0.6-sources.jar
#   structured-fields-0.6-sources.jar.asc
#   structured-fields-0.6-javadoc.jar
#   structured-fields-0.6-javadoc.jar.asc
#   structured-fields-0.6.pom
#   structured-fields-0.6.pom.asc
#
# Usage:
#
#   ./make-central-zip.sh
#
# Or explicitly:
#
#   VERSION=0.6 ./make-central-zip.sh
#

GROUP_ID="${GROUP_ID:-org.greenbytes.http}"
ARTIFACT_ID="${ARTIFACT_ID:-structured-fields}"

VERSION="${VERSION:-$(mvn -q \
    -DforceStdout \
    help:evaluate \
    -Dexpression=project.version)}"

BUILD_DIR="target/central-bundle"
ZIP_FILE="target/${ARTIFACT_ID}-${VERSION}-central.zip"

REPO_PATH="${GROUP_ID//./\/}/${ARTIFACT_ID}/${VERSION}"
DEST="${BUILD_DIR}/${REPO_PATH}"

ARTIFACTS=(
    "${ARTIFACT_ID}-${VERSION}.jar"
    "${ARTIFACT_ID}-${VERSION}-sources.jar"
    "${ARTIFACT_ID}-${VERSION}-javadoc.jar"
    "${ARTIFACT_ID}-${VERSION}.pom"
)


# ----------------------------------------------------------------------
# Checks
# ----------------------------------------------------------------------

command -v mvn >/dev/null 2>&1 || {
    echo "ERROR: mvn not found" >&2
    exit 1
}

command -v gpg >/dev/null 2>&1 || {
    echo "ERROR: gpg not found" >&2
    exit 1
}

command -v zip >/dev/null 2>&1 || {
    echo "ERROR: zip not found" >&2
    exit 1
}

command -v unzip >/dev/null 2>&1 || {
    echo "ERROR: unzip not found" >&2
    exit 1
}


# ----------------------------------------------------------------------
# Configuration
# ----------------------------------------------------------------------

echo "Maven Central bundle"
echo "===================="
echo
echo "Group ID:    $GROUP_ID"
echo "Artifact ID: $ARTIFACT_ID"
echo "Version:     $VERSION"
echo "Repository:  $REPO_PATH"
echo "Output:      $ZIP_FILE"
echo


# ----------------------------------------------------------------------
# Refuse SNAPSHOT versions
# ----------------------------------------------------------------------

if [[ "$VERSION" == *-SNAPSHOT ]]; then
    echo "ERROR: refusing to create a Central release bundle from:" >&2
    echo "       $VERSION" >&2
    echo >&2
    echo "Use a release version first." >&2
    exit 1
fi


# ----------------------------------------------------------------------
# Clean previous bundle
# ----------------------------------------------------------------------

echo "Cleaning previous bundle..."

rm -rf "$BUILD_DIR"
rm -f "$ZIP_FILE"

mkdir -p "$DEST"


# ----------------------------------------------------------------------
# Check existing artifacts and signatures
# ----------------------------------------------------------------------

echo
echo "Checking artifacts and signatures..."

for artifact in "${ARTIFACTS[@]}"; do

    artifact_path="target/$artifact"
    signature_path="target/$artifact.asc"

    if [[ ! -f "$artifact_path" ]]; then
        echo "ERROR: missing artifact:"
        echo "       $artifact_path"
        exit 1
    fi

    if [[ ! -f "$signature_path" ]]; then
        echo "ERROR: missing signature:"
        echo "       $signature_path"
        exit 1
    fi

    echo "  OK  $artifact"
    echo "  OK  $artifact.asc"
done


# ----------------------------------------------------------------------
# Copy artifacts and existing signatures
# ----------------------------------------------------------------------

echo
echo "Copying artifacts..."

for artifact in "${ARTIFACTS[@]}"; do

    cp "target/$artifact" \
       "$DEST/$artifact"

    cp "target/$artifact.asc" \
       "$DEST/$artifact.asc"

done


# ----------------------------------------------------------------------
# Verify GPG signatures
# ----------------------------------------------------------------------

echo
echo "Verifying GPG signatures..."

for artifact in "${ARTIFACTS[@]}"; do

    echo "  verifying $artifact"

    gpg --verify \
        "$DEST/$artifact.asc" \
        "$DEST/$artifact"

done


# ----------------------------------------------------------------------
# Generate checksums
# ----------------------------------------------------------------------

echo
echo "Generating checksums..."

for artifact in "${ARTIFACTS[@]}"; do

    (
        cd "$DEST"

        md5sum "$artifact" \
            | awk '{print $1}' \
            > "$artifact.md5"

        sha1sum "$artifact" \
            | awk '{print $1}' \
            > "$artifact.sha1"

        sha256sum "$artifact" \
            | awk '{print $1}' \
            > "$artifact.sha256"

        sha512sum "$artifact" \
            | awk '{print $1}' \
            > "$artifact.sha512"
    )

    echo "  $artifact"
done


# ----------------------------------------------------------------------
# Verify checksums
# ----------------------------------------------------------------------

echo
echo "Verifying checksums..."

for artifact in "${ARTIFACTS[@]}"; do

    (
        cd "$DEST"

        expected="$(cat "$artifact.md5")"
        actual="$(md5sum "$artifact" | awk '{print $1}')"

        [[ "$expected" == "$actual" ]] || {
            echo "ERROR: MD5 mismatch: $artifact" >&2
            exit 1
        }


        expected="$(cat "$artifact.sha1")"
        actual="$(sha1sum "$artifact" | awk '{print $1}')"

        [[ "$expected" == "$actual" ]] || {
            echo "ERROR: SHA-1 mismatch: $artifact" >&2
            exit 1
        }


        expected="$(cat "$artifact.sha256")"
        actual="$(sha256sum "$artifact" | awk '{print $1}')"

        [[ "$expected" == "$actual" ]] || {
            echo "ERROR: SHA-256 mismatch: $artifact" >&2
            exit 1
        }


        expected="$(cat "$artifact.sha512")"
        actual="$(sha512sum "$artifact" | awk '{print $1}')"

        [[ "$expected" == "$actual" ]] || {
            echo "ERROR: SHA-512 mismatch: $artifact" >&2
            exit 1
        }
    )

    echo "  OK  $artifact"
done


# ----------------------------------------------------------------------
# Show bundle contents before ZIP
# ----------------------------------------------------------------------

echo
echo "Bundle contents:"

find "$BUILD_DIR" \
    -type f \
    -print \
    | sort


# ----------------------------------------------------------------------
# Create ZIP
# ----------------------------------------------------------------------

echo
echo "Creating ZIP..."

rm -f "$ZIP_FILE"

(
    cd "$BUILD_DIR"

    zip -q -r \
        "../$(basename "$ZIP_FILE")" \
        .
)


# ----------------------------------------------------------------------
# Verify ZIP exists
# ----------------------------------------------------------------------

if [[ ! -f "$ZIP_FILE" ]]; then
    echo "ERROR: ZIP was not created:"
    echo "       $ZIP_FILE"
    exit 1
fi


# ----------------------------------------------------------------------
# Test ZIP integrity
# ----------------------------------------------------------------------

echo
echo "Testing ZIP..."

unzip -t "$ZIP_FILE" >/dev/null

echo "  ZIP integrity: OK"


# ----------------------------------------------------------------------
# Verify ZIP layout
# ----------------------------------------------------------------------

echo
echo "ZIP contents:"
echo

unzip -l "$ZIP_FILE"


# ----------------------------------------------------------------------
# Final result
# ----------------------------------------------------------------------

echo
echo "========================================"
echo "SUCCESS"
echo "========================================"
echo
echo "Bundle:"
echo "  $ZIP_FILE"
echo
echo "Size:"
du -h "$ZIP_FILE" | awk '{print "  " $1}'
echo
echo "Maven repository path:"
echo "  $REPO_PATH"
echo