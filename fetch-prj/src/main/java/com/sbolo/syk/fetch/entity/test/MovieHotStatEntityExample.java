package com.sbolo.syk.fetch.entity.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieHotStatEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MovieHotStatEntityExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPrnIsNull() {
            addCriterion("prn is null");
            return (Criteria) this;
        }

        public Criteria andPrnIsNotNull() {
            addCriterion("prn is not null");
            return (Criteria) this;
        }

        public Criteria andPrnEqualTo(String value) {
            addCriterion("prn =", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnNotEqualTo(String value) {
            addCriterion("prn <>", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnGreaterThan(String value) {
            addCriterion("prn >", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnGreaterThanOrEqualTo(String value) {
            addCriterion("prn >=", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnLessThan(String value) {
            addCriterion("prn <", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnLessThanOrEqualTo(String value) {
            addCriterion("prn <=", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnLike(String value) {
            addCriterion("prn like", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnNotLike(String value) {
            addCriterion("prn not like", value, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnIn(List<String> values) {
            addCriterion("prn in", values, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnNotIn(List<String> values) {
            addCriterion("prn not in", values, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnBetween(String value1, String value2) {
            addCriterion("prn between", value1, value2, "prn");
            return (Criteria) this;
        }

        public Criteria andPrnNotBetween(String value1, String value2) {
            addCriterion("prn not between", value1, value2, "prn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnIsNull() {
            addCriterion("movie_prn is null");
            return (Criteria) this;
        }

        public Criteria andMoviePrnIsNotNull() {
            addCriterion("movie_prn is not null");
            return (Criteria) this;
        }

        public Criteria andMoviePrnEqualTo(String value) {
            addCriterion("movie_prn =", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnNotEqualTo(String value) {
            addCriterion("movie_prn <>", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnGreaterThan(String value) {
            addCriterion("movie_prn >", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnGreaterThanOrEqualTo(String value) {
            addCriterion("movie_prn >=", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnLessThan(String value) {
            addCriterion("movie_prn <", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnLessThanOrEqualTo(String value) {
            addCriterion("movie_prn <=", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnLike(String value) {
            addCriterion("movie_prn like", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnNotLike(String value) {
            addCriterion("movie_prn not like", value, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnIn(List<String> values) {
            addCriterion("movie_prn in", values, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnNotIn(List<String> values) {
            addCriterion("movie_prn not in", values, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnBetween(String value1, String value2) {
            addCriterion("movie_prn between", value1, value2, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andMoviePrnNotBetween(String value1, String value2) {
            addCriterion("movie_prn not between", value1, value2, "moviePrn");
            return (Criteria) this;
        }

        public Criteria andPureNameIsNull() {
            addCriterion("pure_name is null");
            return (Criteria) this;
        }

        public Criteria andPureNameIsNotNull() {
            addCriterion("pure_name is not null");
            return (Criteria) this;
        }

        public Criteria andPureNameEqualTo(String value) {
            addCriterion("pure_name =", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameNotEqualTo(String value) {
            addCriterion("pure_name <>", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameGreaterThan(String value) {
            addCriterion("pure_name >", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameGreaterThanOrEqualTo(String value) {
            addCriterion("pure_name >=", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameLessThan(String value) {
            addCriterion("pure_name <", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameLessThanOrEqualTo(String value) {
            addCriterion("pure_name <=", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameLike(String value) {
            addCriterion("pure_name like", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameNotLike(String value) {
            addCriterion("pure_name not like", value, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameIn(List<String> values) {
            addCriterion("pure_name in", values, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameNotIn(List<String> values) {
            addCriterion("pure_name not in", values, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameBetween(String value1, String value2) {
            addCriterion("pure_name between", value1, value2, "pureName");
            return (Criteria) this;
        }

        public Criteria andPureNameNotBetween(String value1, String value2) {
            addCriterion("pure_name not between", value1, value2, "pureName");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIsNull() {
            addCriterion("release_time is null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIsNotNull() {
            addCriterion("release_time is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeEqualTo(Date value) {
            addCriterion("release_time =", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotEqualTo(Date value) {
            addCriterion("release_time <>", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeGreaterThan(Date value) {
            addCriterion("release_time >", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("release_time >=", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeLessThan(Date value) {
            addCriterion("release_time <", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeLessThanOrEqualTo(Date value) {
            addCriterion("release_time <=", value, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeIn(List<Date> values) {
            addCriterion("release_time in", values, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotIn(List<Date> values) {
            addCriterion("release_time not in", values, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeBetween(Date value1, Date value2) {
            addCriterion("release_time between", value1, value2, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeNotBetween(Date value1, Date value2) {
            addCriterion("release_time not between", value1, value2, "releaseTime");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreIsNull() {
            addCriterion("douban_score is null");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreIsNotNull() {
            addCriterion("douban_score is not null");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreEqualTo(BigDecimal value) {
            addCriterion("douban_score =", value, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreNotEqualTo(BigDecimal value) {
            addCriterion("douban_score <>", value, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreGreaterThan(BigDecimal value) {
            addCriterion("douban_score >", value, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("douban_score >=", value, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreLessThan(BigDecimal value) {
            addCriterion("douban_score <", value, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreLessThanOrEqualTo(BigDecimal value) {
            addCriterion("douban_score <=", value, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreIn(List<BigDecimal> values) {
            addCriterion("douban_score in", values, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreNotIn(List<BigDecimal> values) {
            addCriterion("douban_score not in", values, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("douban_score between", value1, value2, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andDoubanScoreNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("douban_score not between", value1, value2, "doubanScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreIsNull() {
            addCriterion("imdb_score is null");
            return (Criteria) this;
        }

        public Criteria andImdbScoreIsNotNull() {
            addCriterion("imdb_score is not null");
            return (Criteria) this;
        }

        public Criteria andImdbScoreEqualTo(BigDecimal value) {
            addCriterion("imdb_score =", value, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreNotEqualTo(BigDecimal value) {
            addCriterion("imdb_score <>", value, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreGreaterThan(BigDecimal value) {
            addCriterion("imdb_score >", value, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("imdb_score >=", value, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreLessThan(BigDecimal value) {
            addCriterion("imdb_score <", value, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreLessThanOrEqualTo(BigDecimal value) {
            addCriterion("imdb_score <=", value, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreIn(List<BigDecimal> values) {
            addCriterion("imdb_score in", values, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreNotIn(List<BigDecimal> values) {
            addCriterion("imdb_score not in", values, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("imdb_score between", value1, value2, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andImdbScoreNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("imdb_score not between", value1, value2, "imdbScore");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIsNull() {
            addCriterion("trigger_type is null");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIsNotNull() {
            addCriterion("trigger_type is not null");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeEqualTo(Integer value) {
            addCriterion("trigger_type =", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotEqualTo(Integer value) {
            addCriterion("trigger_type <>", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeGreaterThan(Integer value) {
            addCriterion("trigger_type >", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("trigger_type >=", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLessThan(Integer value) {
            addCriterion("trigger_type <", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeLessThanOrEqualTo(Integer value) {
            addCriterion("trigger_type <=", value, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeIn(List<Integer> values) {
            addCriterion("trigger_type in", values, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotIn(List<Integer> values) {
            addCriterion("trigger_type not in", values, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeBetween(Integer value1, Integer value2) {
            addCriterion("trigger_type between", value1, value2, "triggerType");
            return (Criteria) this;
        }

        public Criteria andTriggerTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("trigger_type not between", value1, value2, "triggerType");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}