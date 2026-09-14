// everything on the backend is cents (longs), this is the one place
// we turn that back into a dollar-and-cents string for display
export function formatCents(amountCents) {
  const sign = amountCents < 0 ? "-" : "";
  const abs = Math.abs(amountCents);
  const dollars = Math.floor(abs / 100);
  const cents = String(abs % 100).padStart(2, "0");
  return `${sign}$${dollars}.${cents}`;
}

export function toCents(dollarString) {
  const value = Number.parseFloat(dollarString);
  if (Number.isNaN(value)) {
    return 0;
  }
  return Math.round(value * 100);
}
