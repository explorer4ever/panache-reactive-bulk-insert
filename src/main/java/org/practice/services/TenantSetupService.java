package org.practice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;

import org.practice.models.TableA;
import org.practice.models.TableAccount;
import org.practice.models.TableB;
import org.practice.models.TableC;
import org.practice.models.TableD;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

@RequestScoped
public class TenantSetupService {
  private static final String ACCOUNT_ID_1 = "accountId=?1";

  @ReactiveTransactional
  public Uni<Boolean> initializeTenantData() {
    Sort sort = Sort.by("id").ascending();
    TableAccount account = new TableAccount();
    account.setName("Test Account");
    return account.persistAndFlush().chain(persistedAccount -> {
      Integer accountId = ((TableAccount) persistedAccount).getId();
      return TableA.find("accountId is NULL", sort)
          .list()
          .chain(
              genericTableAEntryEntities -> {
                List<TableA> genericTableAEntries = genericTableAEntryEntities.stream()
                    .map(TableA.class::cast).collect(Collectors.toList());
                return setupTableA(genericTableAEntries, accountId)
                    .chain(actTableEntries -> Uni.createFrom().item(true));
              });
    });
  }

  private Uni<Object> setupTableA(List<TableA> genericTableAEntries, Integer accountId) {
    List<TableA> accountTableAEntries = new ArrayList<>();
    for (var genericTableEntry : genericTableAEntries) {
      TableA accountTableAEntry = new TableA();
      accountTableAEntry.setAccountId(accountId);
      accountTableAEntry.setData(genericTableEntry.getData());
      accountTableAEntries.add(accountTableAEntry);
    }
    return TableA.persist(accountTableAEntries).chain(done -> {
      return TableA.find(ACCOUNT_ID_1, accountId).list()
          .chain(
              accountEntries -> {
                List<TableA> actTableAEntries = accountEntries.stream()
                    .map(TableA.class::cast)
                    .collect(Collectors.toList());
                return Uni.createFrom()
                    .item(actTableAEntries)
                    .onItem()
                    .transformToMulti(
                        actTableAEntry -> Multi.createFrom().iterable(actTableAEntry))
                    .onItem()
                    .transformToUniAndMerge(
                        actTableAEntry -> {
                          TableA genericTableAEntry = genericTableAEntries.stream().filter(
                              genericTableA -> genericTableA.getData()
                                  .equals(actTableAEntry.getData()))
                              .findFirst().orElse(null);
                          return TableB
                              .find("accountId is NULL AND aid = ?1", genericTableAEntry.getId())
                              .list().chain(genericTableBEntryEntities -> {
                                List<TableB> genericTableBEntries = genericTableBEntryEntities.stream()
                                    .map(TableB.class::cast)
                                    .collect(Collectors.toList());
                                return setupTableB(
                                    genericTableBEntries, actTableAEntry, genericTableAEntry, accountId)
                                    .onItem().transform(bSetupComplete -> actTableAEntry);
                              });
                        })
                    .collect()
                    .asList();
              });
    });
  }

  private Uni<Object> setupTableB(List<TableB> genericTableBEntries, TableA actTableAEntry, TableA genericTableAEntry,
      Integer accountId) {
    List<TableB> accountTableBEntries = new ArrayList<>();
    for (var genericTableEntry : genericTableBEntries) {
      TableB accountTableBEntry = new TableB();
      accountTableBEntry.setAccountId(accountId);
      accountTableBEntry.setData(genericTableEntry.getData());
      accountTableBEntry.setAid(actTableAEntry.getId());
      accountTableBEntries.add(accountTableBEntry);
    }
    return TableB.persist(accountTableBEntries).chain(done -> {
      return TableB.find(ACCOUNT_ID_1, accountId).list()
          .chain(
              accountEntries -> {
                List<TableB> actTableBEntries = accountEntries.stream()
                    .map(TableB.class::cast)
                    .collect(Collectors.toList());
                return Uni.createFrom()
                    .item(actTableBEntries)
                    .onItem()
                    .transformToMulti(
                        actTableBEntry -> Multi.createFrom().iterable(actTableBEntry))
                    .onItem()
                    .transformToUniAndMerge(
                        actTableBEntry -> {
                          TableB genericTableBEntry = genericTableBEntries.stream().filter(
                              genericTableB -> genericTableB.getData()
                                  .equals(actTableBEntry.getData()))
                              .findFirst().orElse(null);
                          return TableC
                              .find("accountId is NULL AND bid = ?1",
                                  genericTableBEntry.getId())
                              .list()
                              .chain(genericTableCEntryEntities -> {
                                List<TableC> genericTableCEntries = genericTableCEntryEntities.stream()
                                    .map(TableC.class::cast)
                                    .collect(Collectors.toList());
                                return setupTableC(
                                    genericTableCEntries, actTableBEntry, genericTableBEntry, accountId);
                              });
                        })
                    .collect()
                    .asList();
              });
    });

  }

  private Uni<Object> setupTableC(List<TableC> genericTableCEntries, TableB actTableBEntry,
      TableB genericTableBEntry, Integer accountId) {
    List<TableC> accountTableCEntries = new ArrayList<>();
    for (var genericTableEntry : genericTableCEntries) {
      TableC accountTableCEntry = new TableC();
      accountTableCEntry.setAccountId(accountId);
      accountTableCEntry.setData(genericTableEntry.getData());
      accountTableCEntry.setAid(actTableBEntry.getAid());
      accountTableCEntry.setBid(actTableBEntry.getId());
      accountTableCEntries.add(accountTableCEntry);
    }
    return TableC.persist(accountTableCEntries).chain(done -> {
      return TableC.find(ACCOUNT_ID_1, accountId).list()
          .chain(
              accountEntries -> {
                List<TableC> actTableCEntries = accountEntries.stream()
                    .map(TableC.class::cast)
                    .collect(Collectors.toList());
                return Uni.createFrom()
                    .item(actTableCEntries)
                    .onItem()
                    .transformToMulti(
                        actTableCEntry -> Multi.createFrom().iterable(actTableCEntry))
                    .onItem()
                    .transformToUniAndMerge(
                        actTableCEntry -> {
                          TableC genericTableCEntry = genericTableCEntries.stream().filter(
                              genericTableC -> genericTableC.getData()
                                  .equals(actTableCEntry.getData()))
                              .findFirst().orElse(null);
                          return TableD
                              .find("accountId is NULL AND cid = ?1",
                                  genericTableCEntry.getId())
                              .list()
                              .chain(genericTableDEntryEntities -> {
                                List<TableD> genericTableDEntries = genericTableDEntryEntities.stream()
                                    .map(TableD.class::cast)
                                    .collect(Collectors.toList());
                                return setupTableD(
                                    genericTableDEntries, actTableCEntry, genericTableCEntry, accountId);
                              });
                        })
                    .collect()
                    .asList();
              });
    });
  }

  private Uni<? extends Object> setupTableD(List<TableD> genericTableDEntries, TableC actTableCEntry,
      TableC genericTableCEntry, Integer accountId) {
    List<TableD> accountTableDEntries = new ArrayList<>();
    for (var genericTableEntry : genericTableDEntries) {
      TableD accountTableDEntry = new TableD();
      accountTableDEntry.setAccountId(accountId);
      accountTableDEntry.setData(genericTableEntry.getData());
      accountTableDEntry.setAid(actTableCEntry.getAid());
      accountTableDEntry.setBid(actTableCEntry.getBid());
      accountTableDEntry.setCid(actTableCEntry.getId());
      accountTableDEntries.add(accountTableDEntry);
    }
    return TableC.persist(accountTableDEntries);
  }
}
