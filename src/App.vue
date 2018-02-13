<template>
  <!-- App Root Element -->
  <div id="app">
    <!-- Status bar overlay for full screen mode (Cordova or PhoneGap) -->
    <f7-statusbar></f7-statusbar>
    <!-- Main view -->
    <f7-view main :pushState="true">
      <!-- Initial Page -->
      <div class="page">
        <Navbar/>
        <f7-toolbar tabbar labels>
          <f7-link icon="tabIcon ion-compass" text="发现" tab-link="#tab-discover" tab-link-active></f7-link>
          <f7-link icon="tabIcon ion-android-favorite" text="关注" tab-link="#tab-follow"></f7-link>
          <f7-link icon="tabIcon ion-android-contact" text="我" tab-link="#tab-mine"></f7-link>
        </f7-toolbar>
        <f7-tabs animated>
          <f7-tab id="tab-discover" tab-active>
            <DiscoverPage/>
          </f7-tab>
          <f7-tab id="tab-follow">
            <!-- <FollowPage/> -->
          </f7-tab>
          <f7-tab id="tab-mine">
            <MinePage/>
          </f7-tab>
        </f7-tabs>
      </div>
    </f7-view>
  </div>
</template>

<script>
import Navbar from "./components/navbar/navbar.vue";
import DiscoverPage from "./pages/discover.vue";
import FollowPage from "./pages/follow.vue";
import MinePage from "./pages/mine.vue";

export default {
  components: {
    Navbar,
    DiscoverPage,
    FollowPage,
    MinePage
  },
}
</script>


<style>
.tabIcon {
  font-size: 1.6rem;
}
</style>
