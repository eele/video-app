npm run build
cd edu.zhku.video
rm -Force *.apk
rm -Recurse -Force ./app/dist
cp -Recurse -Force ../dist ./app/dist
crosswalk-pkg app
cd ..
