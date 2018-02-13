<template>
  <video id="video" class="video" controls="controls"></video>
</template>

<script>
import Hls from "hls.js";
export default {
  data() {
    return {
      video: "",
      videoUrl: "http://192.168.0.149/playlist.m3u8",
      isFullScreen: "false",
      ui: {}
    };
  },
  mounted() {
    var self = this;
    this.video = document.getElementById("video");
    if (Hls.isSupported()) {
      var hls = new Hls();
      hls.loadSource(this.videoUrl);
      hls.attachMedia(this.video);
      this.video.play();
      // hls.on(Hls.Events.MANIFEST_PARSED, function() {
      //   this.video.play();
      // });
    } else if (video.canPlayType("application/vnd.apple.mpegurl")) {
      // hls.js is not supported on platforms that do not have Media Source Extensions (MSE) enabled.
      // When the browser has built-in HLS support (check using `canPlayType`), we can provide an HLS manifest (i.e. .m3u8 URL) directly to the video element throught the `src` property.
      // This is using the built-in support of the plain video element, without using hls.js.
      this.video.src = this.videoUrl;
      this.video.addEventListener("canplay", function() {
        this.video.play();
      });
    }
    window.addEventListener(
      "deviceorientation",
      function(event) {
        if (event.beta > 30 && event.beta < 150) {
          self.exitFullScreen();
        }
        if (event.gamma > 30 && event.gamma < 90) {
          self.requestFullScreen("rotateLeft");
        }
        if (event.gamma > -90 && event.gamma < -30) {
          self.requestFullScreen("rotateRight");
        }
      },
      true
    );
  },
  methods: {
    requestFullScreen(style) {
      if (this.isFullScreen != style) {
        this.$emit("hideNavbar", this.ui, true);

        this.video.className = "";
        this.video.style.height = document.body.clientWidth + "px";
        this.video.style.width = document.body.clientHeight + "px";
        this.video.style.marginTop =
          (document.body.clientHeight - this.video.offsetHeight) / 2 + "px";
        this.video.style.marginLeft =
          (document.body.clientWidth - this.video.offsetWidth) / 2 + "px";
        this.video.classList.add(style);

        this.isFullScreen = style;
      }
    },
    exitFullScreen() {
      if (this.isFullScreen != "false") {
        this.$emit("hideNavbar", this.ui, false);

        this.video.className = "";
        this.video.style.height = "";
        this.video.style.width = "";
        this.video.style.marginTop = "";
        this.video.style.marginLeft = "";
        this.video.classList.add("video");

        this.isFullScreen = "false";
      }
    },
    onF7Ready($f7) {
      this.ui = $f7;
    }
  }
};
</script>

<style>
#video {
  background-color: black;
  transition: width, height, margin-top, margin-left 200ms, 200ms, 200ms, 200ms;
}
.video {
  width: 100%;
  height: 13rem;
}
.rotateLeft {
  position: fixed;
  object-fit: fill;
  transform: rotate(-90deg);
  -moz-transform: rotate(-90deg); /* Firefox */
  -webkit-transform: rotate(-90deg); /* Safari 和 Chrome */
  -o-transform: rotate(-90deg); /* Opera */
}
.rotateRight {
  position: fixed;
  object-fit: fill;
  transform: rotate(90deg);
  -moz-transform: rotate(90deg); /* Firefox */
  -webkit-transform: rotate(90deg); /* Safari 和 Chrome */
  -o-transform: rotate(90deg); /* Opera */
}
</style>
