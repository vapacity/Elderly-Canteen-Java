<!-- components/Carousel.vue -->
<template>
  <div class="main-section">
    <div class="buttons" @click="prevSlide">
      <img id="slideLeftArrow" src="../assets/left-arrow.png" alt="arrow">
    </div>

    <div class="intro">
      <div class="text-carousel">
        <div
          v-for="(item, index) in texts"
          :key="index"
          :class="['text-slide', { active: currentIndex === index }]"
          :style="item.style"
        >
          <span v-html="item.text"></span>
        </div>
        <div
          v-for="(item, index) in subtexts"
          :key="index"
          :class="['text-slide', { active: currentIndex === index }]"
          :style="item.style"
        >
          <span v-html="item.text"></span>
        </div>
      </div>
    </div>

    <div class="showImg">
      <div class="image-carousel">
        <img
          v-for="(image, index) in images"
          :key="index"
          :src="image"
          :class="['image-slide', { active: currentIndex === index }]"
        />
      </div>
    </div>
    <div class="buttons" @click="nextSlide">
      <img id="slideRightArrow" src="../assets/right-arrow.png" alt="arrow">
    </div>
  </div>
</template>

<script>
export default {
  props: {
    texts: Array,
    subtexts: Array,
    images: Array,
  },
  data() {
    return {
      currentIndex: 0,
      timer: null,
      leftArrow: require('@/assets/left-arrow.png'),  
      leftArrowHover: require('@/assets/left-arrow-hover.png'),
      rightArrow: require('@/assets/right-arrow.png'),  
      rightArrowHover: require('@/assets/right-arrow-hover.png')
    };
  },
  methods: {
    nextSlide() {
      this.currentIndex = (this.currentIndex + 1) % this.texts.length;
    },
    prevSlide() {
      this.currentIndex =
        (this.currentIndex - 1 + this.texts.length) % this.texts.length;
    },
    startAutoSlide() {
      this.timer = setInterval(this.nextSlide, 3000);
    },
    stopAutoSlide() {
      clearInterval(this.timer);
    },
    changeLeftArrowImage(hover) {  
      const leftArrow = document.getElementById('slideLeftArrow');
      leftArrow.src = hover ? this.leftArrowHover : this.leftArrow; 
    },
    changeRightArrowImage(hover) {    
      const rightArrow = document.getElementById('slideRightArrow');  
      rightArrow.src = hover ? this.rightArrowHover : this.rightArrow; 
    }
  },
  mounted() {
    const leftArrow = document.getElementById('slideLeftArrow');  
    const rightArrow = document.getElementById('slideRightArrow');
    leftArrow.addEventListener('mouseover', () => {  
      this.changeLeftArrowImage(true);  
    }); 
    rightArrow.addEventListener('mouseover', () => {  
      this.changeRightArrowImage(true);  
    });  
    leftArrow.addEventListener('mouseout', () => {  
      this.changeLeftArrowImage(false);  
    });
    rightArrow.addEventListener('mouseout', () => {  
      this.changeRightArrowImage(false);  
    });
    this.startAutoSlide();
  },
  beforeUnmount() {
    this.stopAutoSlide();
  },
};
</script>

<style scoped>
/* Carousel 相关样式 */
.main-section {
  background-image: url("@/assets/bg.png");
  background-size: 100% auto;
  background-repeat: no-repeat;
  background-position: top;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: -2px;
}

.intro,
.showImg {
  width: 50%;
}

.image-carousel {
  position: relative;
  height: 450px; /* 根据需要调整高度 */
  overflow: hidden;
  border-radius: 15px;
}

.image-slide {
  position: absolute;
  top: 0;  
  left: 100%; /* 初始位置在容器的右侧 */
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 1.0s ease-in-out, opacity 1.0s ease-in-out;
  opacity: 0; 
}

@keyframes slide-left {  
  0% { transform: translateX(100%); }  
  100% { transform: translateX(-100%); }  
}  
  
.active {  
  /* 可能需要一些额外的样式来确保当前项显示正确 */  
  position: relative; /* 确保当前项在顶层 */  
  z-index: 1;  
}

.image-slide.active {  
  left: 0; /* 滑动到容器的左侧 */  
  opacity: 1;  
}

.text-slide {
  display: none;
  transition: transform 1.0s ease-in-out, opacity 1.0s ease-in-out;
  opacity: 0; 
}

.text-slide.active {
  display: block;
  left: 0; /* 滑动到容器的左侧 */  
  opacity: 1;
} 

.buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 60px;
  height: 60px;
  border-radius: 60px;
  margin: 5vw;
  overflow: hidden;
}

.buttons img {
  width: 100%;
  height: auto;
}
</style>
