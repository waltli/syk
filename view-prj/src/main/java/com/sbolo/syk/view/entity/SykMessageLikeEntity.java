package com.sbolo.syk.view.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "`syk_message_like`")
public class SykMessageLikeEntity {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`prn`")
    private String prn;

    @Column(name = "`msg_prn`")
    private String msgPrn;

    @Column(name = "`gaver_prn`")
    private String gaverPrn;

    @Column(name = "`gaver_ip`")
    private String gaverIp;

    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`update_time`")
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


    public String getMsgPrn() {
		return msgPrn;
	}

	public void setMsgPrn(String msgPrn) {
		this.msgPrn = msgPrn;
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