#!/usr/bin/env bash

# if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
openssl aes-256-cbc -K $encrypted_118457b0408e_key -iv $encrypted_118457b0408e_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
gpg2 --fast-import cd/codesigning.asc --quiet --batch
# fi
