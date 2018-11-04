package com.sbolo.syk.view.vo;

import java.util.Date;

public class SykMessageLikeVO {
	
    private Long id;

    private String prn;

    private String messagePrn;

    private String gaverPrn;

    private String gaverIp;

    private Date createTime;

    private Date updateTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return prn
     */
    public String getPrn() {
        return prn;
    }

    /**
     * @param prn
     */
    public void setPrn(String prn) {
        this.prn = prn;
    }

    /**
     * @return message_prn
     */
    public String getMessagePrn() {
        return messagePrn;
    }

    /**
     * @param messagePrn
     */
    public void setMessagePrn(String messagePrn) {
        this.messagePrn = messagePrn;
    }

    /**
     * @return gaver_prn
     */
    public String getGaverPrn() {
        return gaverPrn;
    }

    /**
     * @param gaverPrn
     */
    public void setGaverPrn(String gaverPrn) {
        this.gaverPrn = gaverPrn;
    }

    /**
     * @return gaver_ip
     */
    public String getGaverIp() {
        return gaverIp;
    }

    /**
     * @param gaverIp
     */
    public void setGaverIp(String gaverIp) {
        this.gaverIp = gaverIp;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}