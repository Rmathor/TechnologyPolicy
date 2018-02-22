package com.mathor.technologypolicy.domain;

import java.util.List;

/**
 * Author: mathor
 * Date : on 2017/11/5 19:18
 * 通知的实体类
 */

public class Inform {

    /**
     * data : {"rows":[{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94686,"issuDate":"2017-11-03","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于公布2017年度第二批广东省众创空间试点单位 国家级科技企业孵化器培育单位的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅产学研结合处","id":94682,"issuDate":"2017-11-03","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于广东省2017年新型研发机构名单的公示","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94643,"issuDate":"2017-11-02","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于华南理工大学等单位实验动物许可事项的公告","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94642,"issuDate":"2017-11-02","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于注销广州医科大学等实验动物许可的公告","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94622,"issuDate":"2017-11-01","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于2017年度国家众创空间备案推荐名单的公示","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅科技交流合作处","id":94604,"issuDate":"2017-10-30","newsContent":"","newsContentArr":null,"newsTitle":"关于加拿大工业废水、市政污水处理技术交流工作坊暨商务对接会的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅政策法规处","id":94594,"issuDate":"2017-10-26","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于遴选第三方机构开展广东省企业研究开发省级财政补助资金绩效评估工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅政策法规处","id":94593,"issuDate":"2017-10-26","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于遴选第三方机构开展2017年度广东省企业研究开发省级财政补助资金管理服务工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94582,"issuDate":"2017-10-25","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅 广东省卫生和计划生育委员会 广东省教育厅 广东省农业厅 广东出入境检验检疫局 广东省食品药品监督管理局 广东省中医药局 中国人民解放军广州军区善后办保障组卫生处关于废止《广东省实验动物许可证管理细则（试行）》的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94522,"issuDate":"2017-10-24","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于2017年第三季度广东省实验动物监督检测结果的公告","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94462,"issuDate":"2017-10-20","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于开展在孵企业、毕业企业、创业导师登记以及孵化器、众创空间数据更新工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅科技交流合作处","id":94443,"issuDate":"2017-10-20","newsContent":"","newsContentArr":null,"newsTitle":"关于转发科技部国际合作司关于征集中国-斯洛文尼亚科技合作委员会第12届例会交流项目的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅科技交流合作处","id":94442,"issuDate":"2017-10-20","newsContent":"","newsContentArr":null,"newsTitle":"关于转发科技部关于2017年度两岸、内地与澳门联合资助研发项目申报时间延长的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅产学研结合处","id":94435,"issuDate":"2017-10-19","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于征集广东省科技军民融合咨询专家库专家的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94422,"issuDate":"2017-10-18","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于开展2017年度国家众创空间备案推荐工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94577,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c新能源汽车\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94576,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c高性能计算\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94575,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c战略性先进电子材料\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94574,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c地球观测与导航\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94573,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c煤炭清洁高效利用和新型节能技术\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94572,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c云计算和大数据\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94571,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c增材制造与激光制造\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94570,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c先进轨道交通\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94569,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c材料基因工程关键技术与支撑平台\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94568,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c网络空间安全\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94567,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c智能电网技术与装备\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94566,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c干细胞及转化研究\u201d试点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94565,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c量子调控与量子信息\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94564,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c纳米科技\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94563,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c蛋白质机器与生命过程调控\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"}],"total":30}
     * msg : null
     * ret : 0
     */

    private DataBean data;
    private Object msg;
    private String ret;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public static class DataBean {
        /**
         * rows : [{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94686,"issuDate":"2017-11-03","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于公布2017年度第二批广东省众创空间试点单位 国家级科技企业孵化器培育单位的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅产学研结合处","id":94682,"issuDate":"2017-11-03","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于广东省2017年新型研发机构名单的公示","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94643,"issuDate":"2017-11-02","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于华南理工大学等单位实验动物许可事项的公告","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94642,"issuDate":"2017-11-02","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于注销广州医科大学等实验动物许可的公告","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94622,"issuDate":"2017-11-01","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于2017年度国家众创空间备案推荐名单的公示","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅科技交流合作处","id":94604,"issuDate":"2017-10-30","newsContent":"","newsContentArr":null,"newsTitle":"关于加拿大工业废水、市政污水处理技术交流工作坊暨商务对接会的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅政策法规处","id":94594,"issuDate":"2017-10-26","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于遴选第三方机构开展广东省企业研究开发省级财政补助资金绩效评估工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅政策法规处","id":94593,"issuDate":"2017-10-26","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于遴选第三方机构开展2017年度广东省企业研究开发省级财政补助资金管理服务工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94582,"issuDate":"2017-10-25","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅 广东省卫生和计划生育委员会 广东省教育厅 广东省农业厅 广东出入境检验检疫局 广东省食品药品监督管理局 广东省中医药局 中国人民解放军广州军区善后办保障组卫生处关于废止《广东省实验动物许可证管理细则（试行）》的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅基础研究与科研条件处","id":94522,"issuDate":"2017-10-24","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于2017年第三季度广东省实验动物监督检测结果的公告","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94462,"issuDate":"2017-10-20","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于开展在孵企业、毕业企业、创业导师登记以及孵化器、众创空间数据更新工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅科技交流合作处","id":94443,"issuDate":"2017-10-20","newsContent":"","newsContentArr":null,"newsTitle":"关于转发科技部国际合作司关于征集中国-斯洛文尼亚科技合作委员会第12届例会交流项目的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅科技交流合作处","id":94442,"issuDate":"2017-10-20","newsContent":"","newsContentArr":null,"newsTitle":"关于转发科技部关于2017年度两岸、内地与澳门联合资助研发项目申报时间延长的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅产学研结合处","id":94435,"issuDate":"2017-10-19","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于征集广东省科技军民融合咨询专家库专家的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"广东省科技厅高新技术发展及产业化处","id":94422,"issuDate":"2017-10-18","newsContent":"","newsContentArr":null,"newsTitle":"广东省科学技术厅关于开展2017年度国家众创空间备案推荐工作的通知","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94577,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c新能源汽车\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94576,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c高性能计算\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94575,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c战略性先进电子材料\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94574,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c地球观测与导航\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94573,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c煤炭清洁高效利用和新型节能技术\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94572,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c云计算和大数据\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94571,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c增材制造与激光制造\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94570,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c先进轨道交通\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94569,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c材料基因工程关键技术与支撑平台\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94568,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c网络空间安全\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94567,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c智能电网技术与装备\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94566,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c干细胞及转化研究\u201d试点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94565,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c量子调控与量子信息\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94564,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c纳米科技\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"},{"comeFrom":"科学技术部","id":94563,"issuDate":"2017-10-16","newsContent":"","newsContentArr":null,"newsTitle":"\u201c蛋白质机器与生命过程调控\u201d重点专项2018年度项目申报指南","picDescription":null,"picUrl":null,"type":"0"}]
         * total : 30
         */

        private int total;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * comeFrom : 广东省科技厅高新技术发展及产业化处
             * id : 94686
             * issuDate : 2017-11-03
             * newsContent :
             * newsContentArr : null
             * newsTitle : 广东省科学技术厅关于公布2017年度第二批广东省众创空间试点单位 国家级科技企业孵化器培育单位的通知
             * picDescription : null
             * picUrl : null
             * type : 0
             */

            private String comeFrom;
            private int id;
            private String issuDate;
            private String newsContent;
            private Object newsContentArr;
            private String newsTitle;
            private Object picDescription;
            private Object picUrl;
            private String type;

            public String getComeFrom() {
                return comeFrom;
            }

            public void setComeFrom(String comeFrom) {
                this.comeFrom = comeFrom;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getIssuDate() {
                return issuDate;
            }

            public void setIssuDate(String issuDate) {
                this.issuDate = issuDate;
            }

            public String getNewsContent() {
                return newsContent;
            }

            public void setNewsContent(String newsContent) {
                this.newsContent = newsContent;
            }

            public Object getNewsContentArr() {
                return newsContentArr;
            }

            public void setNewsContentArr(Object newsContentArr) {
                this.newsContentArr = newsContentArr;
            }

            public String getNewsTitle() {
                return newsTitle;
            }

            public void setNewsTitle(String newsTitle) {
                this.newsTitle = newsTitle;
            }

            public Object getPicDescription() {
                return picDescription;
            }

            public void setPicDescription(Object picDescription) {
                this.picDescription = picDescription;
            }

            public Object getPicUrl() {
                return picUrl;
            }

            public void setPicUrl(Object picUrl) {
                this.picUrl = picUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
