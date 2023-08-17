package com.bulgarian.lawerapp.adapters;

import com.bulgarian.lawerapp.model.caseinfo.BeHalfOf;
import com.bulgarian.lawerapp.model.caseinfo.CaseType;
import com.bulgarian.lawerapp.model.caseinfo.DeadlineList;
import com.bulgarian.lawerapp.model.caseinfo.Instance;
import com.bulgarian.lawerapp.model.caseinfo.TypeJudgment;
import com.bulgarian.lawerapp.model.caseinfo.ClientList;
import com.bulgarian.lawerapp.model.caseinfo.CourtId;
import com.bulgarian.lawerapp.model.caseinfo.HearingList;
import com.bulgarian.lawerapp.model.caseinfo.RespondList;

import java.util.ArrayList;

public class CaseListGetSet {

    public String Id;
    public String UserId;
    public String CaseNo;
    public String year;
    public CaseType caseType;
    public Instance instance;
    public String refer_case_id;
    public String contractor_judge;
    public String contr_judge_contect;
    public String contr_judge_email;
    public String judge;
    public CourtId courtId;
    public BeHalfOf beHalfOf;
    public String dispute_for;
    public String client_id;
    public String amount;
    public String price;
    public TypeJudgment typeJudgment;

    public String type_judgment_no;
    public String took_effect;
    public String appeal_caase_n;
    public String notes;
    public String status;
    public String archive;
    public String strtotime;
    public ArrayList<ClientList> clientList;
    public ArrayList<RespondList> respondList;
    public ArrayList<HearingList> hearingList;
    public ArrayList<DeadlineList> deadlineList;


    public String Date;
    public String HearingDate;
    public String Deadline;
    public String CaseInfo;
    public String clientName;
    public String hearing;


    public CaseListGetSet(String caseNo, String hearingDate, String deadline, String caseInfo, String clientName) {
        CaseNo = caseNo;
        HearingDate = hearingDate;
        Deadline = deadline;
        CaseInfo = caseInfo;
        this.clientName = clientName;
    }

    public CaseListGetSet(String id, String userId, String caseNo, String year, String refer_case_id, String contractor_judge, String contr_judge_contect, String contr_judge_email, String judge, String dispute_for, String client_id, String amount, String price, String type_judgment_no, String took_effect) {
        Id = id;
        UserId = userId;
        CaseNo = caseNo;
        this.year = year;
        this.refer_case_id = refer_case_id;
        this.contractor_judge = contractor_judge;
        this.contr_judge_contect = contr_judge_contect;
        this.contr_judge_email = contr_judge_email;
        this.judge = judge;
        this.dispute_for = dispute_for;
        this.client_id = client_id;
        this.amount = amount;
        this.price = price;
        this.type_judgment_no = type_judgment_no;
        this.took_effect = took_effect;

    }


    public CaseListGetSet() {

    }

    public ArrayList<DeadlineList> getDeadlineList() {
        return deadlineList;
    }

    public void setDeadlineList(ArrayList<DeadlineList> deadlineList) {
        this.deadlineList = deadlineList;
    }

    public ArrayList<HearingList> getHearingList() {
        return hearingList;
    }

    public void setHearingList(ArrayList<HearingList> hearingList) {
        this.hearingList = hearingList;
    }

    public ArrayList<RespondList> getRespondList() {
        return respondList;
    }

    public void setRespondList(ArrayList<RespondList> respondList) {
        this.respondList = respondList;
    }

    public ArrayList<ClientList> getClientList() {
        return clientList;
    }

    public void setClientList(ArrayList<ClientList> clientList) {
        this.clientList = clientList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType_judgment_no() {
        return type_judgment_no;
    }

    public void setAppeal_caase_n(String appeal_caase_n) {
        this.appeal_caase_n = appeal_caase_n;
    }

    public String getAppeal_caase_n() {
        return appeal_caase_n;
    }

    public void setTook_effect(String took_effect) {
        this.took_effect = took_effect;
    }

    public String getNotes() {
        return notes;
    }

    public void setArchive(String archive) {
        this.archive = archive;
    }

    public String getArchive() {
        return archive;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTook_effect() {
        return took_effect;
    }

    public void setType_judgment_no(String type_judgment_no) {
        this.type_judgment_no = type_judgment_no;
    }

    public String getStrtotime() {
        return strtotime;
    }

    public void setStrtotime(String strtotime) {
        this.strtotime = strtotime;
    }


    public TypeJudgment getTypeJudgment() {
        return typeJudgment;
    }

    public void setTypeJudgment(TypeJudgment typeJudgment) {
        this.typeJudgment = typeJudgment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getDispute_for() {
        return dispute_for;
    }

    public void setDispute_for(String dispute_for) {
        this.dispute_for = dispute_for;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BeHalfOf getBeHalfOf() {
        return beHalfOf;
    }

    public void setBeHalfOf(BeHalfOf beHalfOf) {
        this.beHalfOf = beHalfOf;
    }

    public CourtId getCourtId() {
        return courtId;
    }

    public void setCourtId(CourtId courtId) {
        this.courtId = courtId;
    }

    public String getContr_judge_contect() {
        return contr_judge_contect;
    }

    public void setContr_judge_contect(String contr_judge_contect) {
        this.contr_judge_contect = contr_judge_contect;
    }

    public String getContr_judge_email() {
        return contr_judge_email;
    }

    public void setContr_judge_email(String contr_judge_email) {
        this.contr_judge_email = contr_judge_email;
    }

    public String getContractor_judge() {
        return contractor_judge;
    }

    public void setContractor_judge(String contractor_judge) {
        this.contractor_judge = contractor_judge;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getRefer_case_id() {
        return refer_case_id;
    }

    public void setRefer_case_id(String refer_case_id) {
        this.refer_case_id = refer_case_id;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }

    public String getHearing() {
        return hearing;
    }

    public void setHearing(String hearing) {
        this.hearing = hearing;
    }

    public CaseType getCaseType() {
        return caseType;
    }

    public void setCaseType(CaseType caseType) {
        this.caseType = caseType;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCaseNo() {
        return CaseNo;
    }

    public void setCaseNo(String caseNo) {
        CaseNo = caseNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHearingDate() {
        return HearingDate;
    }

    public void setHearingDate(String hearingDate) {
        HearingDate = hearingDate;
    }

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public String getCaseInfo() {
        return CaseInfo;
    }

    public void setCaseInfo(String caseInfo) {
        CaseInfo = caseInfo;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
