package com.sbolo.syk.fetch.entity.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieInfoEntityExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MovieInfoEntityExample() {
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

        public Criteria andIconUriIsNull() {
            addCriterion("icon_uri is null");
            return (Criteria) this;
        }

        public Criteria andIconUriIsNotNull() {
            addCriterion("icon_uri is not null");
            return (Criteria) this;
        }

        public Criteria andIconUriEqualTo(String value) {
            addCriterion("icon_uri =", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriNotEqualTo(String value) {
            addCriterion("icon_uri <>", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriGreaterThan(String value) {
            addCriterion("icon_uri >", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriGreaterThanOrEqualTo(String value) {
            addCriterion("icon_uri >=", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriLessThan(String value) {
            addCriterion("icon_uri <", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriLessThanOrEqualTo(String value) {
            addCriterion("icon_uri <=", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriLike(String value) {
            addCriterion("icon_uri like", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriNotLike(String value) {
            addCriterion("icon_uri not like", value, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriIn(List<String> values) {
            addCriterion("icon_uri in", values, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriNotIn(List<String> values) {
            addCriterion("icon_uri not in", values, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriBetween(String value1, String value2) {
            addCriterion("icon_uri between", value1, value2, "iconUri");
            return (Criteria) this;
        }

        public Criteria andIconUriNotBetween(String value1, String value2) {
            addCriterion("icon_uri not between", value1, value2, "iconUri");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonIsNull() {
            addCriterion("poster_uri_json is null");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonIsNotNull() {
            addCriterion("poster_uri_json is not null");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonEqualTo(String value) {
            addCriterion("poster_uri_json =", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonNotEqualTo(String value) {
            addCriterion("poster_uri_json <>", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonGreaterThan(String value) {
            addCriterion("poster_uri_json >", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonGreaterThanOrEqualTo(String value) {
            addCriterion("poster_uri_json >=", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonLessThan(String value) {
            addCriterion("poster_uri_json <", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonLessThanOrEqualTo(String value) {
            addCriterion("poster_uri_json <=", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonLike(String value) {
            addCriterion("poster_uri_json like", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonNotLike(String value) {
            addCriterion("poster_uri_json not like", value, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonIn(List<String> values) {
            addCriterion("poster_uri_json in", values, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonNotIn(List<String> values) {
            addCriterion("poster_uri_json not in", values, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonBetween(String value1, String value2) {
            addCriterion("poster_uri_json between", value1, value2, "posterUriJson");
            return (Criteria) this;
        }

        public Criteria andPosterUriJsonNotBetween(String value1, String value2) {
            addCriterion("poster_uri_json not between", value1, value2, "posterUriJson");
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

        public Criteria andAnotherNameIsNull() {
            addCriterion("another_name is null");
            return (Criteria) this;
        }

        public Criteria andAnotherNameIsNotNull() {
            addCriterion("another_name is not null");
            return (Criteria) this;
        }

        public Criteria andAnotherNameEqualTo(String value) {
            addCriterion("another_name =", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameNotEqualTo(String value) {
            addCriterion("another_name <>", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameGreaterThan(String value) {
            addCriterion("another_name >", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameGreaterThanOrEqualTo(String value) {
            addCriterion("another_name >=", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameLessThan(String value) {
            addCriterion("another_name <", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameLessThanOrEqualTo(String value) {
            addCriterion("another_name <=", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameLike(String value) {
            addCriterion("another_name like", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameNotLike(String value) {
            addCriterion("another_name not like", value, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameIn(List<String> values) {
            addCriterion("another_name in", values, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameNotIn(List<String> values) {
            addCriterion("another_name not in", values, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameBetween(String value1, String value2) {
            addCriterion("another_name between", value1, value2, "anotherName");
            return (Criteria) this;
        }

        public Criteria andAnotherNameNotBetween(String value1, String value2) {
            addCriterion("another_name not between", value1, value2, "anotherName");
            return (Criteria) this;
        }

        public Criteria andLabelsIsNull() {
            addCriterion("labels is null");
            return (Criteria) this;
        }

        public Criteria andLabelsIsNotNull() {
            addCriterion("labels is not null");
            return (Criteria) this;
        }

        public Criteria andLabelsEqualTo(String value) {
            addCriterion("labels =", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotEqualTo(String value) {
            addCriterion("labels <>", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsGreaterThan(String value) {
            addCriterion("labels >", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsGreaterThanOrEqualTo(String value) {
            addCriterion("labels >=", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLessThan(String value) {
            addCriterion("labels <", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLessThanOrEqualTo(String value) {
            addCriterion("labels <=", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLike(String value) {
            addCriterion("labels like", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotLike(String value) {
            addCriterion("labels not like", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsIn(List<String> values) {
            addCriterion("labels in", values, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotIn(List<String> values) {
            addCriterion("labels not in", values, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsBetween(String value1, String value2) {
            addCriterion("labels between", value1, value2, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotBetween(String value1, String value2) {
            addCriterion("labels not between", value1, value2, "labels");
            return (Criteria) this;
        }

        public Criteria andDirectorsIsNull() {
            addCriterion("directors is null");
            return (Criteria) this;
        }

        public Criteria andDirectorsIsNotNull() {
            addCriterion("directors is not null");
            return (Criteria) this;
        }

        public Criteria andDirectorsEqualTo(String value) {
            addCriterion("directors =", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsNotEqualTo(String value) {
            addCriterion("directors <>", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsGreaterThan(String value) {
            addCriterion("directors >", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsGreaterThanOrEqualTo(String value) {
            addCriterion("directors >=", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsLessThan(String value) {
            addCriterion("directors <", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsLessThanOrEqualTo(String value) {
            addCriterion("directors <=", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsLike(String value) {
            addCriterion("directors like", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsNotLike(String value) {
            addCriterion("directors not like", value, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsIn(List<String> values) {
            addCriterion("directors in", values, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsNotIn(List<String> values) {
            addCriterion("directors not in", values, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsBetween(String value1, String value2) {
            addCriterion("directors between", value1, value2, "directors");
            return (Criteria) this;
        }

        public Criteria andDirectorsNotBetween(String value1, String value2) {
            addCriterion("directors not between", value1, value2, "directors");
            return (Criteria) this;
        }

        public Criteria andWritersIsNull() {
            addCriterion("writers is null");
            return (Criteria) this;
        }

        public Criteria andWritersIsNotNull() {
            addCriterion("writers is not null");
            return (Criteria) this;
        }

        public Criteria andWritersEqualTo(String value) {
            addCriterion("writers =", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersNotEqualTo(String value) {
            addCriterion("writers <>", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersGreaterThan(String value) {
            addCriterion("writers >", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersGreaterThanOrEqualTo(String value) {
            addCriterion("writers >=", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersLessThan(String value) {
            addCriterion("writers <", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersLessThanOrEqualTo(String value) {
            addCriterion("writers <=", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersLike(String value) {
            addCriterion("writers like", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersNotLike(String value) {
            addCriterion("writers not like", value, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersIn(List<String> values) {
            addCriterion("writers in", values, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersNotIn(List<String> values) {
            addCriterion("writers not in", values, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersBetween(String value1, String value2) {
            addCriterion("writers between", value1, value2, "writers");
            return (Criteria) this;
        }

        public Criteria andWritersNotBetween(String value1, String value2) {
            addCriterion("writers not between", value1, value2, "writers");
            return (Criteria) this;
        }

        public Criteria andCastsIsNull() {
            addCriterion("casts is null");
            return (Criteria) this;
        }

        public Criteria andCastsIsNotNull() {
            addCriterion("casts is not null");
            return (Criteria) this;
        }

        public Criteria andCastsEqualTo(String value) {
            addCriterion("casts =", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsNotEqualTo(String value) {
            addCriterion("casts <>", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsGreaterThan(String value) {
            addCriterion("casts >", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsGreaterThanOrEqualTo(String value) {
            addCriterion("casts >=", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsLessThan(String value) {
            addCriterion("casts <", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsLessThanOrEqualTo(String value) {
            addCriterion("casts <=", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsLike(String value) {
            addCriterion("casts like", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsNotLike(String value) {
            addCriterion("casts not like", value, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsIn(List<String> values) {
            addCriterion("casts in", values, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsNotIn(List<String> values) {
            addCriterion("casts not in", values, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsBetween(String value1, String value2) {
            addCriterion("casts between", value1, value2, "casts");
            return (Criteria) this;
        }

        public Criteria andCastsNotBetween(String value1, String value2) {
            addCriterion("casts not between", value1, value2, "casts");
            return (Criteria) this;
        }

        public Criteria andLocationsIsNull() {
            addCriterion("locations is null");
            return (Criteria) this;
        }

        public Criteria andLocationsIsNotNull() {
            addCriterion("locations is not null");
            return (Criteria) this;
        }

        public Criteria andLocationsEqualTo(String value) {
            addCriterion("locations =", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsNotEqualTo(String value) {
            addCriterion("locations <>", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsGreaterThan(String value) {
            addCriterion("locations >", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsGreaterThanOrEqualTo(String value) {
            addCriterion("locations >=", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsLessThan(String value) {
            addCriterion("locations <", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsLessThanOrEqualTo(String value) {
            addCriterion("locations <=", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsLike(String value) {
            addCriterion("locations like", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsNotLike(String value) {
            addCriterion("locations not like", value, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsIn(List<String> values) {
            addCriterion("locations in", values, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsNotIn(List<String> values) {
            addCriterion("locations not in", values, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsBetween(String value1, String value2) {
            addCriterion("locations between", value1, value2, "locations");
            return (Criteria) this;
        }

        public Criteria andLocationsNotBetween(String value1, String value2) {
            addCriterion("locations not between", value1, value2, "locations");
            return (Criteria) this;
        }

        public Criteria andLanguagesIsNull() {
            addCriterion("languages is null");
            return (Criteria) this;
        }

        public Criteria andLanguagesIsNotNull() {
            addCriterion("languages is not null");
            return (Criteria) this;
        }

        public Criteria andLanguagesEqualTo(String value) {
            addCriterion("languages =", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesNotEqualTo(String value) {
            addCriterion("languages <>", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesGreaterThan(String value) {
            addCriterion("languages >", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesGreaterThanOrEqualTo(String value) {
            addCriterion("languages >=", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesLessThan(String value) {
            addCriterion("languages <", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesLessThanOrEqualTo(String value) {
            addCriterion("languages <=", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesLike(String value) {
            addCriterion("languages like", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesNotLike(String value) {
            addCriterion("languages not like", value, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesIn(List<String> values) {
            addCriterion("languages in", values, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesNotIn(List<String> values) {
            addCriterion("languages not in", values, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesBetween(String value1, String value2) {
            addCriterion("languages between", value1, value2, "languages");
            return (Criteria) this;
        }

        public Criteria andLanguagesNotBetween(String value1, String value2) {
            addCriterion("languages not between", value1, value2, "languages");
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

        public Criteria andReleaseTimeFormatIsNull() {
            addCriterion("release_time_format is null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatIsNotNull() {
            addCriterion("release_time_format is not null");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatEqualTo(String value) {
            addCriterion("release_time_format =", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatNotEqualTo(String value) {
            addCriterion("release_time_format <>", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatGreaterThan(String value) {
            addCriterion("release_time_format >", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatGreaterThanOrEqualTo(String value) {
            addCriterion("release_time_format >=", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatLessThan(String value) {
            addCriterion("release_time_format <", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatLessThanOrEqualTo(String value) {
            addCriterion("release_time_format <=", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatLike(String value) {
            addCriterion("release_time_format like", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatNotLike(String value) {
            addCriterion("release_time_format not like", value, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatIn(List<String> values) {
            addCriterion("release_time_format in", values, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatNotIn(List<String> values) {
            addCriterion("release_time_format not in", values, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatBetween(String value1, String value2) {
            addCriterion("release_time_format between", value1, value2, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andReleaseTimeFormatNotBetween(String value1, String value2) {
            addCriterion("release_time_format not between", value1, value2, "releaseTimeFormat");
            return (Criteria) this;
        }

        public Criteria andYearIsNull() {
            addCriterion("year is null");
            return (Criteria) this;
        }

        public Criteria andYearIsNotNull() {
            addCriterion("year is not null");
            return (Criteria) this;
        }

        public Criteria andYearEqualTo(String value) {
            addCriterion("year =", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotEqualTo(String value) {
            addCriterion("year <>", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThan(String value) {
            addCriterion("year >", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThanOrEqualTo(String value) {
            addCriterion("year >=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThan(String value) {
            addCriterion("year <", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThanOrEqualTo(String value) {
            addCriterion("year <=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLike(String value) {
            addCriterion("year like", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotLike(String value) {
            addCriterion("year not like", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearIn(List<String> values) {
            addCriterion("year in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotIn(List<String> values) {
            addCriterion("year not in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearBetween(String value1, String value2) {
            addCriterion("year between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotBetween(String value1, String value2) {
            addCriterion("year not between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andDurationIsNull() {
            addCriterion("duration is null");
            return (Criteria) this;
        }

        public Criteria andDurationIsNotNull() {
            addCriterion("duration is not null");
            return (Criteria) this;
        }

        public Criteria andDurationEqualTo(String value) {
            addCriterion("duration =", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotEqualTo(String value) {
            addCriterion("duration <>", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThan(String value) {
            addCriterion("duration >", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationGreaterThanOrEqualTo(String value) {
            addCriterion("duration >=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThan(String value) {
            addCriterion("duration <", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLessThanOrEqualTo(String value) {
            addCriterion("duration <=", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationLike(String value) {
            addCriterion("duration like", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotLike(String value) {
            addCriterion("duration not like", value, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationIn(List<String> values) {
            addCriterion("duration in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotIn(List<String> values) {
            addCriterion("duration not in", values, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationBetween(String value1, String value2) {
            addCriterion("duration between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andDurationNotBetween(String value1, String value2) {
            addCriterion("duration not between", value1, value2, "duration");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNull() {
            addCriterion("summary is null");
            return (Criteria) this;
        }

        public Criteria andSummaryIsNotNull() {
            addCriterion("summary is not null");
            return (Criteria) this;
        }

        public Criteria andSummaryEqualTo(String value) {
            addCriterion("summary =", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotEqualTo(String value) {
            addCriterion("summary <>", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThan(String value) {
            addCriterion("summary >", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryGreaterThanOrEqualTo(String value) {
            addCriterion("summary >=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThan(String value) {
            addCriterion("summary <", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLessThanOrEqualTo(String value) {
            addCriterion("summary <=", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryLike(String value) {
            addCriterion("summary like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotLike(String value) {
            addCriterion("summary not like", value, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryIn(List<String> values) {
            addCriterion("summary in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotIn(List<String> values) {
            addCriterion("summary not in", values, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryBetween(String value1, String value2) {
            addCriterion("summary between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andSummaryNotBetween(String value1, String value2) {
            addCriterion("summary not between", value1, value2, "summary");
            return (Criteria) this;
        }

        public Criteria andDoubanIdIsNull() {
            addCriterion("douban_id is null");
            return (Criteria) this;
        }

        public Criteria andDoubanIdIsNotNull() {
            addCriterion("douban_id is not null");
            return (Criteria) this;
        }

        public Criteria andDoubanIdEqualTo(String value) {
            addCriterion("douban_id =", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdNotEqualTo(String value) {
            addCriterion("douban_id <>", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdGreaterThan(String value) {
            addCriterion("douban_id >", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdGreaterThanOrEqualTo(String value) {
            addCriterion("douban_id >=", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdLessThan(String value) {
            addCriterion("douban_id <", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdLessThanOrEqualTo(String value) {
            addCriterion("douban_id <=", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdLike(String value) {
            addCriterion("douban_id like", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdNotLike(String value) {
            addCriterion("douban_id not like", value, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdIn(List<String> values) {
            addCriterion("douban_id in", values, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdNotIn(List<String> values) {
            addCriterion("douban_id not in", values, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdBetween(String value1, String value2) {
            addCriterion("douban_id between", value1, value2, "doubanId");
            return (Criteria) this;
        }

        public Criteria andDoubanIdNotBetween(String value1, String value2) {
            addCriterion("douban_id not between", value1, value2, "doubanId");
            return (Criteria) this;
        }

        public Criteria andImdbIdIsNull() {
            addCriterion("imdb_id is null");
            return (Criteria) this;
        }

        public Criteria andImdbIdIsNotNull() {
            addCriterion("imdb_id is not null");
            return (Criteria) this;
        }

        public Criteria andImdbIdEqualTo(String value) {
            addCriterion("imdb_id =", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdNotEqualTo(String value) {
            addCriterion("imdb_id <>", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdGreaterThan(String value) {
            addCriterion("imdb_id >", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdGreaterThanOrEqualTo(String value) {
            addCriterion("imdb_id >=", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdLessThan(String value) {
            addCriterion("imdb_id <", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdLessThanOrEqualTo(String value) {
            addCriterion("imdb_id <=", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdLike(String value) {
            addCriterion("imdb_id like", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdNotLike(String value) {
            addCriterion("imdb_id not like", value, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdIn(List<String> values) {
            addCriterion("imdb_id in", values, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdNotIn(List<String> values) {
            addCriterion("imdb_id not in", values, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdBetween(String value1, String value2) {
            addCriterion("imdb_id between", value1, value2, "imdbId");
            return (Criteria) this;
        }

        public Criteria andImdbIdNotBetween(String value1, String value2) {
            addCriterion("imdb_id not between", value1, value2, "imdbId");
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

        public Criteria andAttentionRateIsNull() {
            addCriterion("attention_rate is null");
            return (Criteria) this;
        }

        public Criteria andAttentionRateIsNotNull() {
            addCriterion("attention_rate is not null");
            return (Criteria) this;
        }

        public Criteria andAttentionRateEqualTo(Integer value) {
            addCriterion("attention_rate =", value, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateNotEqualTo(Integer value) {
            addCriterion("attention_rate <>", value, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateGreaterThan(Integer value) {
            addCriterion("attention_rate >", value, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateGreaterThanOrEqualTo(Integer value) {
            addCriterion("attention_rate >=", value, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateLessThan(Integer value) {
            addCriterion("attention_rate <", value, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateLessThanOrEqualTo(Integer value) {
            addCriterion("attention_rate <=", value, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateIn(List<Integer> values) {
            addCriterion("attention_rate in", values, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateNotIn(List<Integer> values) {
            addCriterion("attention_rate not in", values, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateBetween(Integer value1, Integer value2) {
            addCriterion("attention_rate between", value1, value2, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andAttentionRateNotBetween(Integer value1, Integer value2) {
            addCriterion("attention_rate not between", value1, value2, "attentionRate");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNull() {
            addCriterion("category is null");
            return (Criteria) this;
        }

        public Criteria andCategoryIsNotNull() {
            addCriterion("category is not null");
            return (Criteria) this;
        }

        public Criteria andCategoryEqualTo(Integer value) {
            addCriterion("category =", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotEqualTo(Integer value) {
            addCriterion("category <>", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThan(Integer value) {
            addCriterion("category >", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("category >=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThan(Integer value) {
            addCriterion("category <", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryLessThanOrEqualTo(Integer value) {
            addCriterion("category <=", value, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryIn(List<Integer> values) {
            addCriterion("category in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotIn(List<Integer> values) {
            addCriterion("category not in", values, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryBetween(Integer value1, Integer value2) {
            addCriterion("category between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andCategoryNotBetween(Integer value1, Integer value2) {
            addCriterion("category not between", value1, value2, "category");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonIsNull() {
            addCriterion("present_season is null");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonIsNotNull() {
            addCriterion("present_season is not null");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonEqualTo(Integer value) {
            addCriterion("present_season =", value, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonNotEqualTo(Integer value) {
            addCriterion("present_season <>", value, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonGreaterThan(Integer value) {
            addCriterion("present_season >", value, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonGreaterThanOrEqualTo(Integer value) {
            addCriterion("present_season >=", value, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonLessThan(Integer value) {
            addCriterion("present_season <", value, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonLessThanOrEqualTo(Integer value) {
            addCriterion("present_season <=", value, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonIn(List<Integer> values) {
            addCriterion("present_season in", values, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonNotIn(List<Integer> values) {
            addCriterion("present_season not in", values, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonBetween(Integer value1, Integer value2) {
            addCriterion("present_season between", value1, value2, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andPresentSeasonNotBetween(Integer value1, Integer value2) {
            addCriterion("present_season not between", value1, value2, "presentSeason");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeIsNull() {
            addCriterion("total_episode is null");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeIsNotNull() {
            addCriterion("total_episode is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeEqualTo(Integer value) {
            addCriterion("total_episode =", value, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeNotEqualTo(Integer value) {
            addCriterion("total_episode <>", value, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeGreaterThan(Integer value) {
            addCriterion("total_episode >", value, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_episode >=", value, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeLessThan(Integer value) {
            addCriterion("total_episode <", value, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeLessThanOrEqualTo(Integer value) {
            addCriterion("total_episode <=", value, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeIn(List<Integer> values) {
            addCriterion("total_episode in", values, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeNotIn(List<Integer> values) {
            addCriterion("total_episode not in", values, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeBetween(Integer value1, Integer value2) {
            addCriterion("total_episode between", value1, value2, "totalEpisode");
            return (Criteria) this;
        }

        public Criteria andTotalEpisodeNotBetween(Integer value1, Integer value2) {
            addCriterion("total_episode not between", value1, value2, "totalEpisode");
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

        public Criteria andResourceWriteTimeIsNull() {
            addCriterion("resource_write_time is null");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeIsNotNull() {
            addCriterion("resource_write_time is not null");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeEqualTo(Date value) {
            addCriterion("resource_write_time =", value, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeNotEqualTo(Date value) {
            addCriterion("resource_write_time <>", value, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeGreaterThan(Date value) {
            addCriterion("resource_write_time >", value, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("resource_write_time >=", value, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeLessThan(Date value) {
            addCriterion("resource_write_time <", value, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeLessThanOrEqualTo(Date value) {
            addCriterion("resource_write_time <=", value, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeIn(List<Date> values) {
            addCriterion("resource_write_time in", values, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeNotIn(List<Date> values) {
            addCriterion("resource_write_time not in", values, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeBetween(Date value1, Date value2) {
            addCriterion("resource_write_time between", value1, value2, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andResourceWriteTimeNotBetween(Date value1, Date value2) {
            addCriterion("resource_write_time not between", value1, value2, "resourceWriteTime");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnIsNull() {
            addCriterion("optimal_resource_prn is null");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnIsNotNull() {
            addCriterion("optimal_resource_prn is not null");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnEqualTo(String value) {
            addCriterion("optimal_resource_prn =", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnNotEqualTo(String value) {
            addCriterion("optimal_resource_prn <>", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnGreaterThan(String value) {
            addCriterion("optimal_resource_prn >", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnGreaterThanOrEqualTo(String value) {
            addCriterion("optimal_resource_prn >=", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnLessThan(String value) {
            addCriterion("optimal_resource_prn <", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnLessThanOrEqualTo(String value) {
            addCriterion("optimal_resource_prn <=", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnLike(String value) {
            addCriterion("optimal_resource_prn like", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnNotLike(String value) {
            addCriterion("optimal_resource_prn not like", value, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnIn(List<String> values) {
            addCriterion("optimal_resource_prn in", values, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnNotIn(List<String> values) {
            addCriterion("optimal_resource_prn not in", values, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnBetween(String value1, String value2) {
            addCriterion("optimal_resource_prn between", value1, value2, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andOptimalResourcePrnNotBetween(String value1, String value2) {
            addCriterion("optimal_resource_prn not between", value1, value2, "optimalResourcePrn");
            return (Criteria) this;
        }

        public Criteria andCountClickIsNull() {
            addCriterion("count_click is null");
            return (Criteria) this;
        }

        public Criteria andCountClickIsNotNull() {
            addCriterion("count_click is not null");
            return (Criteria) this;
        }

        public Criteria andCountClickEqualTo(Integer value) {
            addCriterion("count_click =", value, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickNotEqualTo(Integer value) {
            addCriterion("count_click <>", value, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickGreaterThan(Integer value) {
            addCriterion("count_click >", value, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickGreaterThanOrEqualTo(Integer value) {
            addCriterion("count_click >=", value, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickLessThan(Integer value) {
            addCriterion("count_click <", value, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickLessThanOrEqualTo(Integer value) {
            addCriterion("count_click <=", value, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickIn(List<Integer> values) {
            addCriterion("count_click in", values, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickNotIn(List<Integer> values) {
            addCriterion("count_click not in", values, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickBetween(Integer value1, Integer value2) {
            addCriterion("count_click between", value1, value2, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountClickNotBetween(Integer value1, Integer value2) {
            addCriterion("count_click not between", value1, value2, "countClick");
            return (Criteria) this;
        }

        public Criteria andCountCommentIsNull() {
            addCriterion("count_comment is null");
            return (Criteria) this;
        }

        public Criteria andCountCommentIsNotNull() {
            addCriterion("count_comment is not null");
            return (Criteria) this;
        }

        public Criteria andCountCommentEqualTo(Integer value) {
            addCriterion("count_comment =", value, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentNotEqualTo(Integer value) {
            addCriterion("count_comment <>", value, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentGreaterThan(Integer value) {
            addCriterion("count_comment >", value, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentGreaterThanOrEqualTo(Integer value) {
            addCriterion("count_comment >=", value, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentLessThan(Integer value) {
            addCriterion("count_comment <", value, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentLessThanOrEqualTo(Integer value) {
            addCriterion("count_comment <=", value, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentIn(List<Integer> values) {
            addCriterion("count_comment in", values, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentNotIn(List<Integer> values) {
            addCriterion("count_comment not in", values, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentBetween(Integer value1, Integer value2) {
            addCriterion("count_comment between", value1, value2, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountCommentNotBetween(Integer value1, Integer value2) {
            addCriterion("count_comment not between", value1, value2, "countComment");
            return (Criteria) this;
        }

        public Criteria andCountDownloadIsNull() {
            addCriterion("count_download is null");
            return (Criteria) this;
        }

        public Criteria andCountDownloadIsNotNull() {
            addCriterion("count_download is not null");
            return (Criteria) this;
        }

        public Criteria andCountDownloadEqualTo(Integer value) {
            addCriterion("count_download =", value, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadNotEqualTo(Integer value) {
            addCriterion("count_download <>", value, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadGreaterThan(Integer value) {
            addCriterion("count_download >", value, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadGreaterThanOrEqualTo(Integer value) {
            addCriterion("count_download >=", value, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadLessThan(Integer value) {
            addCriterion("count_download <", value, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadLessThanOrEqualTo(Integer value) {
            addCriterion("count_download <=", value, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadIn(List<Integer> values) {
            addCriterion("count_download in", values, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadNotIn(List<Integer> values) {
            addCriterion("count_download not in", values, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadBetween(Integer value1, Integer value2) {
            addCriterion("count_download between", value1, value2, "countDownload");
            return (Criteria) this;
        }

        public Criteria andCountDownloadNotBetween(Integer value1, Integer value2) {
            addCriterion("count_download not between", value1, value2, "countDownload");
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