<template>
    <f7-navbar sliding>
        <!-- <f7-toolbar v-if="false" no-hairline tabbar scrollable class="ca-top">
            <f7-link tab-link="#tab1">Tab 1</f7-link>
            <f7-link tab-link="#tab2">Tab 2</f7-link>
            <f7-link tab-link="#tab1">Tab 1</f7-link>
            <f7-link tab-link="#tab2">Tab 2</f7-link>
            <f7-link tab-link="#tab1">Tab 1</f7-link>
            <f7-link tab-link="#tab2">Tab 2</f7-link>
            <f7-link tab-link="#tab1">Tab 1</f7-link>
            <f7-link tab-link="#tab2">Tab 2</f7-link>
            <f7-link tab-link="#tab1">Tab 1</f7-link>
            <f7-link tab-link="#tab2">Tab 2</f7-link>
        </f7-toolbar> -->
        <f7-nav-right>
            <f7-link icon="ion-ios-search-strong" class="searchIcon searchbar-enable"></f7-link>
        </f7-nav-right>
        <!-- Searchbar -->
        <f7-searchbar expandable backdrop 
        disable-button-text="取消" placeholder="搜搜看 ..." :clear-button="true">
        </f7-searchbar>
    </f7-navbar>
</template>

<style>
.searchIcon {
  font-size: 1.6rem;
}
.ca-top {
  z-index: 1;
}
</style>
