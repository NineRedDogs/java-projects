package com.pragmatists.domain;

import com.pragmatists.eventsourcing.api.AggregateId;
import com.pragmatists.eventsourcing.api.Event;


class AccountDeposited implements Event<Account> {
    private final AccountId id;
    private final Integer amount;

    public AccountDeposited(AccountId id, Integer amount) {

        this.id = id;
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public AggregateId getAggregateId() {
        return id;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public String getEventType() {
        return null;
    }

    @Override
    public void applyOn(Account account) {
        account.apply(this);
    }
}
