package com.sbolo.syk.fetch.entity.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResourceInfoEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ResourceInfoEntityExample() {
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

        public Criteria andSizeIsNull() {
            addCriterion("size is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("size is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(String value) {
            addCriterion("size =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(String value) {
            addCriterion("size <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(String value) {
            addCriterion("size >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(String value) {
            addCriterion("size >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(String value) {
            addCriterion("size <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(String value) {
            addCriterion("size <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLike(String value) {
            addCriterion("size like", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotLike(String value) {
            addCriterion("size not like", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<String> values) {
            addCriterion("size in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<String> values) {
            addCriterion("size not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(String value1, String value2) {
            addCriterion("size between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(String value1, String value2) {
            addCriterion("size not between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andFormatIsNull() {
            addCriterion("format is null");
            return (Criteria) this;
        }

        public Criteria andFormatIsNotNull() {
            addCriterion("format is not null");
            return (Criteria) this;
        }

        public Criteria andFormatEqualTo(String value) {
            addCriterion("format =", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotEqualTo(String value) {
            addCriterion("format <>", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatGreaterThan(String value) {
            addCriterion("format >", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatGreaterThanOrEqualTo(String value) {
            addCriterion("format >=", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatLessThan(String value) {
            addCriterion("format <", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatLessThanOrEqualTo(String value) {
            addCriterion("format <=", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatLike(String value) {
            addCriterion("format like", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotLike(String value) {
            addCriterion("format not like", value, "format");
            return (Criteria) this;
        }

        public Criteria andFormatIn(List<String> values) {
            addCriterion("format in", values, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotIn(List<String> values) {
            addCriterion("format not in", values, "format");
            return (Criteria) this;
        }

        public Criteria andFormatBetween(String value1, String value2) {
            addCriterion("format between", value1, value2, "format");
            return (Criteria) this;
        }

        public Criteria andFormatNotBetween(String value1, String value2) {
            addCriterion("format not between", value1, value2, "format");
            return (Criteria) this;
        }

        public Criteria andDefinitionIsNull() {
            addCriterion("definition is null");
            return (Criteria) this;
        }

        public Criteria andDefinitionIsNotNull() {
            addCriterion("definition is not null");
            return (Criteria) this;
        }

        public Criteria andDefinitionEqualTo(Integer value) {
            addCriterion("definition =", value, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionNotEqualTo(Integer value) {
            addCriterion("definition <>", value, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionGreaterThan(Integer value) {
            addCriterion("definition >", value, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionGreaterThanOrEqualTo(Integer value) {
            addCriterion("definition >=", value, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionLessThan(Integer value) {
            addCriterion("definition <", value, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionLessThanOrEqualTo(Integer value) {
            addCriterion("definition <=", value, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionIn(List<Integer> values) {
            addCriterion("definition in", values, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionNotIn(List<Integer> values) {
            addCriterion("definition not in", values, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionBetween(Integer value1, Integer value2) {
            addCriterion("definition between", value1, value2, "definition");
            return (Criteria) this;
        }

        public Criteria andDefinitionNotBetween(Integer value1, Integer value2) {
            addCriterion("definition not between", value1, value2, "definition");
            return (Criteria) this;
        }

        public Criteria andQualityIsNull() {
            addCriterion("quality is null");
            return (Criteria) this;
        }

        public Criteria andQualityIsNotNull() {
            addCriterion("quality is not null");
            return (Criteria) this;
        }

        public Criteria andQualityEqualTo(String value) {
            addCriterion("quality =", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotEqualTo(String value) {
            addCriterion("quality <>", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityGreaterThan(String value) {
            addCriterion("quality >", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityGreaterThanOrEqualTo(String value) {
            addCriterion("quality >=", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityLessThan(String value) {
            addCriterion("quality <", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityLessThanOrEqualTo(String value) {
            addCriterion("quality <=", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityLike(String value) {
            addCriterion("quality like", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotLike(String value) {
            addCriterion("quality not like", value, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityIn(List<String> values) {
            addCriterion("quality in", values, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotIn(List<String> values) {
            addCriterion("quality not in", values, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityBetween(String value1, String value2) {
            addCriterion("quality between", value1, value2, "quality");
            return (Criteria) this;
        }

        public Criteria andQualityNotBetween(String value1, String value2) {
            addCriterion("quality not between", value1, value2, "quality");
            return (Criteria) this;
        }

        public Criteria andResolutionIsNull() {
            addCriterion("resolution is null");
            return (Criteria) this;
        }

        public Criteria andResolutionIsNotNull() {
            addCriterion("resolution is not null");
            return (Criteria) this;
        }

        public Criteria andResolutionEqualTo(String value) {
            addCriterion("resolution =", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotEqualTo(String value) {
            addCriterion("resolution <>", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionGreaterThan(String value) {
            addCriterion("resolution >", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionGreaterThanOrEqualTo(String value) {
            addCriterion("resolution >=", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionLessThan(String value) {
            addCriterion("resolution <", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionLessThanOrEqualTo(String value) {
            addCriterion("resolution <=", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionLike(String value) {
            addCriterion("resolution like", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotLike(String value) {
            addCriterion("resolution not like", value, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionIn(List<String> values) {
            addCriterion("resolution in", values, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotIn(List<String> values) {
            addCriterion("resolution not in", values, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionBetween(String value1, String value2) {
            addCriterion("resolution between", value1, value2, "resolution");
            return (Criteria) this;
        }

        public Criteria andResolutionNotBetween(String value1, String value2) {
            addCriterion("resolution not between", value1, value2, "resolution");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNull() {
            addCriterion("speed is null");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNotNull() {
            addCriterion("speed is not null");
            return (Criteria) this;
        }

        public Criteria andSpeedEqualTo(Integer value) {
            addCriterion("speed =", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotEqualTo(Integer value) {
            addCriterion("speed <>", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThan(Integer value) {
            addCriterion("speed >", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThanOrEqualTo(Integer value) {
            addCriterion("speed >=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThan(Integer value) {
            addCriterion("speed <", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThanOrEqualTo(Integer value) {
            addCriterion("speed <=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedIn(List<Integer> values) {
            addCriterion("speed in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotIn(List<Integer> values) {
            addCriterion("speed not in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedBetween(Integer value1, Integer value2) {
            addCriterion("speed between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotBetween(Integer value1, Integer value2) {
            addCriterion("speed not between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkIsNull() {
            addCriterion("download_link is null");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkIsNotNull() {
            addCriterion("download_link is not null");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkEqualTo(String value) {
            addCriterion("download_link =", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkNotEqualTo(String value) {
            addCriterion("download_link <>", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkGreaterThan(String value) {
            addCriterion("download_link >", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkGreaterThanOrEqualTo(String value) {
            addCriterion("download_link >=", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkLessThan(String value) {
            addCriterion("download_link <", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkLessThanOrEqualTo(String value) {
            addCriterion("download_link <=", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkLike(String value) {
            addCriterion("download_link like", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkNotLike(String value) {
            addCriterion("download_link not like", value, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkIn(List<String> values) {
            addCriterion("download_link in", values, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkNotIn(List<String> values) {
            addCriterion("download_link not in", values, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkBetween(String value1, String value2) {
            addCriterion("download_link between", value1, value2, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andDownloadLinkNotBetween(String value1, String value2) {
            addCriterion("download_link not between", value1, value2, "downloadLink");
            return (Criteria) this;
        }

        public Criteria andSeasonIsNull() {
            addCriterion("season is null");
            return (Criteria) this;
        }

        public Criteria andSeasonIsNotNull() {
            addCriterion("season is not null");
            return (Criteria) this;
        }

        public Criteria andSeasonEqualTo(Integer value) {
            addCriterion("season =", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonNotEqualTo(Integer value) {
            addCriterion("season <>", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonGreaterThan(Integer value) {
            addCriterion("season >", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonGreaterThanOrEqualTo(Integer value) {
            addCriterion("season >=", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonLessThan(Integer value) {
            addCriterion("season <", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonLessThanOrEqualTo(Integer value) {
            addCriterion("season <=", value, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonIn(List<Integer> values) {
            addCriterion("season in", values, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonNotIn(List<Integer> values) {
            addCriterion("season not in", values, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonBetween(Integer value1, Integer value2) {
            addCriterion("season between", value1, value2, "season");
            return (Criteria) this;
        }

        public Criteria andSeasonNotBetween(Integer value1, Integer value2) {
            addCriterion("season not between", value1, value2, "season");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartIsNull() {
            addCriterion("episode_start is null");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartIsNotNull() {
            addCriterion("episode_start is not null");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartEqualTo(Integer value) {
            addCriterion("episode_start =", value, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartNotEqualTo(Integer value) {
            addCriterion("episode_start <>", value, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartGreaterThan(Integer value) {
            addCriterion("episode_start >", value, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartGreaterThanOrEqualTo(Integer value) {
            addCriterion("episode_start >=", value, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartLessThan(Integer value) {
            addCriterion("episode_start <", value, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartLessThanOrEqualTo(Integer value) {
            addCriterion("episode_start <=", value, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartIn(List<Integer> values) {
            addCriterion("episode_start in", values, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartNotIn(List<Integer> values) {
            addCriterion("episode_start not in", values, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartBetween(Integer value1, Integer value2) {
            addCriterion("episode_start between", value1, value2, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeStartNotBetween(Integer value1, Integer value2) {
            addCriterion("episode_start not between", value1, value2, "episodeStart");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndIsNull() {
            addCriterion("episode_end is null");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndIsNotNull() {
            addCriterion("episode_end is not null");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndEqualTo(Integer value) {
            addCriterion("episode_end =", value, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndNotEqualTo(Integer value) {
            addCriterion("episode_end <>", value, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndGreaterThan(Integer value) {
            addCriterion("episode_end >", value, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndGreaterThanOrEqualTo(Integer value) {
            addCriterion("episode_end >=", value, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndLessThan(Integer value) {
            addCriterion("episode_end <", value, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndLessThanOrEqualTo(Integer value) {
            addCriterion("episode_end <=", value, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndIn(List<Integer> values) {
            addCriterion("episode_end in", values, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndNotIn(List<Integer> values) {
            addCriterion("episode_end not in", values, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndBetween(Integer value1, Integer value2) {
            addCriterion("episode_end between", value1, value2, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andEpisodeEndNotBetween(Integer value1, Integer value2) {
            addCriterion("episode_end not between", value1, value2, "episodeEnd");
            return (Criteria) this;
        }

        public Criteria andSubtitleIsNull() {
            addCriterion("subtitle is null");
            return (Criteria) this;
        }

        public Criteria andSubtitleIsNotNull() {
            addCriterion("subtitle is not null");
            return (Criteria) this;
        }

        public Criteria andSubtitleEqualTo(String value) {
            addCriterion("subtitle =", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleNotEqualTo(String value) {
            addCriterion("subtitle <>", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleGreaterThan(String value) {
            addCriterion("subtitle >", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleGreaterThanOrEqualTo(String value) {
            addCriterion("subtitle >=", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleLessThan(String value) {
            addCriterion("subtitle <", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleLessThanOrEqualTo(String value) {
            addCriterion("subtitle <=", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleLike(String value) {
            addCriterion("subtitle like", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleNotLike(String value) {
            addCriterion("subtitle not like", value, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleIn(List<String> values) {
            addCriterion("subtitle in", values, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleNotIn(List<String> values) {
            addCriterion("subtitle not in", values, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleBetween(String value1, String value2) {
            addCriterion("subtitle between", value1, value2, "subtitle");
            return (Criteria) this;
        }

        public Criteria andSubtitleNotBetween(String value1, String value2) {
            addCriterion("subtitle not between", value1, value2, "subtitle");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonIsNull() {
            addCriterion("printscreen_uri_json is null");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonIsNotNull() {
            addCriterion("printscreen_uri_json is not null");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonEqualTo(String value) {
            addCriterion("printscreen_uri_json =", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonNotEqualTo(String value) {
            addCriterion("printscreen_uri_json <>", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonGreaterThan(String value) {
            addCriterion("printscreen_uri_json >", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonGreaterThanOrEqualTo(String value) {
            addCriterion("printscreen_uri_json >=", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonLessThan(String value) {
            addCriterion("printscreen_uri_json <", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonLessThanOrEqualTo(String value) {
            addCriterion("printscreen_uri_json <=", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonLike(String value) {
            addCriterion("printscreen_uri_json like", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonNotLike(String value) {
            addCriterion("printscreen_uri_json not like", value, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonIn(List<String> values) {
            addCriterion("printscreen_uri_json in", values, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonNotIn(List<String> values) {
            addCriterion("printscreen_uri_json not in", values, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonBetween(String value1, String value2) {
            addCriterion("printscreen_uri_json between", value1, value2, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andPrintscreenUriJsonNotBetween(String value1, String value2) {
            addCriterion("printscreen_uri_json not between", value1, value2, "printscreenUriJson");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlIsNull() {
            addCriterion("come_from_url is null");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlIsNotNull() {
            addCriterion("come_from_url is not null");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlEqualTo(String value) {
            addCriterion("come_from_url =", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlNotEqualTo(String value) {
            addCriterion("come_from_url <>", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlGreaterThan(String value) {
            addCriterion("come_from_url >", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlGreaterThanOrEqualTo(String value) {
            addCriterion("come_from_url >=", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlLessThan(String value) {
            addCriterion("come_from_url <", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlLessThanOrEqualTo(String value) {
            addCriterion("come_from_url <=", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlLike(String value) {
            addCriterion("come_from_url like", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlNotLike(String value) {
            addCriterion("come_from_url not like", value, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlIn(List<String> values) {
            addCriterion("come_from_url in", values, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlNotIn(List<String> values) {
            addCriterion("come_from_url not in", values, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlBetween(String value1, String value2) {
            addCriterion("come_from_url between", value1, value2, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andComeFromUrlNotBetween(String value1, String value2) {
            addCriterion("come_from_url not between", value1, value2, "comeFromUrl");
            return (Criteria) this;
        }

        public Criteria andStIsNull() {
            addCriterion("st is null");
            return (Criteria) this;
        }

        public Criteria andStIsNotNull() {
            addCriterion("st is not null");
            return (Criteria) this;
        }

        public Criteria andStEqualTo(Integer value) {
            addCriterion("st =", value, "st");
            return (Criteria) this;
        }

        public Criteria andStNotEqualTo(Integer value) {
            addCriterion("st <>", value, "st");
            return (Criteria) this;
        }

        public Criteria andStGreaterThan(Integer value) {
            addCriterion("st >", value, "st");
            return (Criteria) this;
        }

        public Criteria andStGreaterThanOrEqualTo(Integer value) {
            addCriterion("st >=", value, "st");
            return (Criteria) this;
        }

        public Criteria andStLessThan(Integer value) {
            addCriterion("st <", value, "st");
            return (Criteria) this;
        }

        public Criteria andStLessThanOrEqualTo(Integer value) {
            addCriterion("st <=", value, "st");
            return (Criteria) this;
        }

        public Criteria andStIn(List<Integer> values) {
            addCriterion("st in", values, "st");
            return (Criteria) this;
        }

        public Criteria andStNotIn(List<Integer> values) {
            addCriterion("st not in", values, "st");
            return (Criteria) this;
        }

        public Criteria andStBetween(Integer value1, Integer value2) {
            addCriterion("st between", value1, value2, "st");
            return (Criteria) this;
        }

        public Criteria andStNotBetween(Integer value1, Integer value2) {
            addCriterion("st not between", value1, value2, "st");
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