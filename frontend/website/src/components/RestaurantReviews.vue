<!-- components/RestaurantReviews.vue -->
<template>
  <section>
    <!-- 锚点，用于使导航栏跳转到本区域上方50px的位置，不然顶部导航栏会遮住部分内容 -->
    <a
      id="reviewsNav"
      style="display: block; height: 50px; margin-top: -50px"
    ></a>
    <div class="reviewsToCanteen">
      <div class="head">
        <div class="canteenScore">
          <h2 id="comprehensiveScoreText">综合评分：</h2>
          <div class="comprehensiveStars">
            <template v-for="n in 5" :key="n">
              <span
                :class="
                  n <= comprehensiveScore ? 'star yellow' : 'star gray'
                "
              ></span>
            </template>
          </div>
          <h2 id="comprehensiveScore">{{ comprehensiveScore }} 分</h2>
        </div>
        <h1>您的评分是我们最大的动力!</h1>
      </div>
      <div class="commentFrame">
        <div
          v-for="(comment, index) in comments"
          :key="index"
          class="singleComment"
        >
          <div class="singleCommentHead">
            <div class="commentStars">
              <template v-for="n in 5" :key="n">
                <span
                  :class="n <= comment.cStars ? 'star yellow' : 'star gray'"
                ></span>
              </template>
            </div>
            <h4 id="commentScore">{{ comment.cStars }} 分</h4>
          </div>
          <div class="commentText">
            {{ comment.cReviewText || "该用户未留下评价文本。" }}
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    comprehensiveScore: Number,
    comments: Array,
  },
};
</script>

<style scoped>
.reviewsToCanteen {
  display: flex;
  flex-direction: column;
}
.head {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.canteenScore {
  display: flex;
  align-items: center;
  margin: 30px;
  background-color: orange;
  padding: 15px 50px;
  height: 50px;
  border-radius: 50px;
  box-shadow: 0 0px 15px rgba(180, 109, 27, 0.5);
}
h2 {
  font-size: 20px;
  font-weight: lighter;
  margin-left: 10px;
}
.star {
  display: inline-block;
  width: 23px;
  height: 23px;
  margin: 3px 2px;
  clip-path: polygon(
    50% 0%,
    61% 35%,
    98% 35%,
    68% 57%,
    79% 91%,
    50% 70%,
    21% 91%,
    32% 57%,
    2% 35%,
    39% 35%
  );
}
.yellow {
  background-color: gold;
}
.gray {
  background-color: lightgray;
}
.commentFrame {
  display: flex;
  padding: 5px 0;
  height: 250px;
  background-color: #ffffff;
  border-radius: 15px;
  overflow-x: auto;
  align-items: center;
  box-sizing: border-box;
  margin: 20px;
}
.commentFrame::-webkit-scrollbar {
  height: 5px;
}
.commentFrame::-webkit-scrollbar-thumb {
  background-color: #cccccc;
  border-radius: 2.5px;
}
.singleComment {
  background-color: white;
  height: 85%;
  flex: 0 0 25%;
  margin-left: 25px;
  border-radius: 5px;
  padding: 0 3%;
  box-sizing: border-box;
  box-shadow: 0 0px 15px rgba(149, 149, 149, 0.3);
}
.singleCommentHead {
  display: flex;
}
.commentStars {
  display: flex;
  margin-top: 25px;
}
.commentStars .star {
  width: 18px;
  height: 18px;
  margin: 5px 1px 0;
}
h4 {
  margin: 30px 0 0 auto;
}
.commentText {
  overflow-y: auto;
}
.commentText::-webkit-scrollbar {
  width: 5px;
}
.commentText::-webkit-scrollbar-thumb {
  background-color: #cccccc;
  border-radius: 2.5px;
}
</style>
