<template>
    <!-- “发现”页面 -->
    <f7-page>
        <Swiper/>
        <VideoCard/>
        <VideoCard/>
    </f7-page>
</template>

<script>
import Swiper from "../components/swiper/swiper.vue";
import VideoCard from "../components/videocard/videocard.vue";
export default {
  components: {
    Swiper,
    VideoCard
  }
};
</script>
