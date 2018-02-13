<template>
  <!-- 视频播放页面 -->
  <div class="page">
    <!-- <f7-navbar v-if="!hideNavbarFlag" sliding title="My App" back-link="Back"></f7-navbar> -->
    <f7-navbar sliding title="My App" back-link="Back"></f7-navbar>
    <div id="con" class="page-content">
      <Player @hideNavbar="hideNavbar" />
    </div>
  </div>
</template>

<script>
import Player from "../components/player/player.vue";
export default {
  components: {
    Player
  },
  data() {
    return {
      hideNavbarFlag: false
    };
  },
  methods: {
    hideNavbar(...flag) {
      this.hideNavbarFlag = flag[1];
      if (flag[1]) {
        flag[0].navbar.hide(".navbar");
        document.getElementById("con").style.paddingTop = "0";
      } else {
        flag[0].navbar.show(".navbar");
        document.getElementById("con").style.paddingTop = "44px";
      }
    }
  }
};
</script>
