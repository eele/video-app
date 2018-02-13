<template>
    <f7-swiper pagination :params="swParams">
        <f7-swiper-slide>
            <div style="height:15rem;background-color:rgb(76, 161, 255)"></div>
        </f7-swiper-slide>
        <f7-swiper-slide>
            <div style="height:15rem;background-color:rgb(255, 224, 102)"></div>
        </f7-swiper-slide>
        <f7-swiper-slide>
            <div style="height:15rem;background-color:rgb(134, 255, 154)"></div>
        </f7-swiper-slide>
    </f7-swiper>
</template>

<script>
export default {
  data() {
    return {
      swParams: {
        speed: 400,
        autoplay: {
          delay: 5000,
          disableOnInteraction: false
        },
        loop: true
      }
    };
  }
};
</script>
