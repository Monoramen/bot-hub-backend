export default function CommandLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <section className=" ">
      <div className=" w-full  text-center justify-center">
        {children}
      </div>
    </section>
  );
}
